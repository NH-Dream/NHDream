package com.ssafy.nhdream.domain.redeposit.service;

import com.ssafy.nhdream.common.drdc.DRDCService;
import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.common.utils.CalculateDate;
import com.ssafy.nhdream.common.utils.CreateAccountNum;
import com.ssafy.nhdream.common.utils.TransferService;
import com.ssafy.nhdream.domain.admin.repository.TaskLogRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositTransactionRepository;
import com.ssafy.nhdream.domain.redeposit.dto.*;
import com.ssafy.nhdream.domain.redeposit.repository.ReDepositAccountRepository;
import com.ssafy.nhdream.domain.redeposit.repository.RedepositOptionRepository;
import com.ssafy.nhdream.domain.redeposit.repository.RedepositProductRepository;
import com.ssafy.nhdream.domain.redeposit.repository.RedepositTransactionRepository;
import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.domain.sse.service.NotificationService;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.admin.JopType;
import com.ssafy.nhdream.entity.admin.TaskLog;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.redeposit.ReDepositAccount;
import com.ssafy.nhdream.entity.redeposit.ReDepositOptions;
import com.ssafy.nhdream.entity.redeposit.ReDepositProduct;
import com.ssafy.nhdream.entity.redeposit.ReDepositTransaction;
import com.ssafy.nhdream.entity.user.User;
import com.ssafy.nhdream.entity.user.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedepositServiceImpl implements RedepositService {

    private final RedepositProductRepository redepositProductRepository;
    private final RedepositOptionRepository redepositOptionRepository;
    private final ReDepositAccountRepository reDepositAccountRepository;
    private final RedepositTransactionRepository redepositTransactionRepository;
    private final UserRepository userRepository;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final NHDCService nhdcService;
    private final RedepositBlockchainService redepositBlockchainService;
    private final TransferService transferService;
    private final FrDepositTransactionRepository frDepositTransactionRepository;
    private final DRDCService drdcService;
    private final TaskLogRepository taskLogRepository;
    private final CreateAccountNum createAccountNum;

    // SSE 설정
    private final NotificationService notificationService;

    @Value("${NHDC.privateKey}")
    private String serverPrivateKey;

    @Value("${redeposit.contractAddress}")
    private String redepositContractAddress;

    @Override
    @Transactional
    public int createRedepositProduct(RedepositProductCreationReqDto redepositProductCreationReqDto) {
        ReDepositProduct reDepositProduct = ReDepositProduct.builder()
                .name(redepositProductCreationReqDto.getName())
                .amountRange(redepositProductCreationReqDto.getMaximum())
                .build();

        ReDepositProduct save = redepositProductRepository.save(reDepositProduct);

        BigDecimal divisor = new BigDecimal("100");
        for(RedepositOptionCreationReqDto r : redepositProductCreationReqDto.getOptions()){
            ReDepositOptions reDepositOptions = ReDepositOptions.builder()
                    .reDepositProduct(save)
                    .rate(r.getOptionRate().divide(divisor, 2, RoundingMode.DOWN))
                    .preferredRate1(r.getOptionPreferredRate1().divide(divisor, 2, RoundingMode.DOWN))
                    .preferredRate2(r.getOptionPreferredRate2().divide(divisor, 2, RoundingMode.DOWN))
                    .term(r.getTerm())
                    .build();

            redepositOptionRepository.save(reDepositOptions);
        }

        return save.getId();

    }

    @Override
    public List<RedepositProductListDto> getRedepositProductList() {

        return redepositProductRepository.findReDepositProductsWithTerm24();
    }

    @Override
    public RedepositProductDetailResDto getRedepositProductDetail(int redepositProductId) {
        ReDepositProduct reDepositProduct = redepositProductRepository.findById(redepositProductId)
                .orElseThrow(() -> new CustomException(ExceptionType.REDEPOSIT_PRODUCT_NOT_EXIST));

        RedepositProductDetailResDto.RedepositProductDetailResDtoBuilder builder = RedepositProductDetailResDto.builder()
                .id(reDepositProduct.getId())
                .name(reDepositProduct.getName())
                .maximum(reDepositProduct.getAmountRange());

        BigDecimal minRate = BigDecimal.ZERO;
        BigDecimal maxRate = BigDecimal.ZERO;

        for(ReDepositOptions reDepositOptions : reDepositProduct.getReDepositOptions()){

            log.info("{}", reDepositOptions.getRate());
            log.info("{}", reDepositOptions.getTerm());
            log.info("{}", reDepositOptions.getPreferredRate1());
            log.info("{}", reDepositOptions.getPreferredRate2());

            BigDecimal graduateRate = reDepositOptions.getRate().add(reDepositOptions.getPreferredRate1()).multiply(new BigDecimal("100"));
            BigDecimal farmerRate = reDepositOptions.getRate().add(reDepositOptions.getPreferredRate2()).multiply(new BigDecimal("100"));
            if (reDepositOptions.getTerm() == 6) {
                minRate = graduateRate.min(farmerRate);
                builder.graduateRate6Months(graduateRate)
                        .farmerRate6Months(farmerRate)
                        .option6Id(reDepositOptions.getId());
            } else if (reDepositOptions.getTerm() == 12) {
                builder.graduateRate12Months(graduateRate)
                        .farmerRate12Months(farmerRate)
                        .option12Id(reDepositOptions.getId());
            } else if (reDepositOptions.getTerm() == 24) {
                maxRate = graduateRate.max(farmerRate);
                builder.graduateRate24Months(graduateRate)
                        .farmerRate24Months(farmerRate)
                        .option24Id(reDepositOptions.getId());
            }
        }

        log.info("{}", minRate);
        log.info("{}", maxRate);

        return builder.minRate(minRate)
                .maxRate(maxRate)
                .build();
    }

    @Override
    @Transactional
    public void openAccount(AccountCreationDto accountCreationDto, int userId, int depositProductId) {

        log.info("Service start : {}", accountCreationDto.getReDepositAmount());

        // 유저
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 비밀번호 검사
        if (!bCryptPasswordEncoder.matches(accountCreationDto.getPassword(), user.getWalletPassword())) {
            throw new CustomException(ExceptionType.WALLETPASSWORD_NOT_MATCH);
        }

        // 해당 옵션 조회
        ReDepositOptions depositOption = redepositOptionRepository.findById(accountCreationDto.getDepositOptionId())
                .orElseThrow(() -> new CustomException(ExceptionType.REDEPOSIT_OPTION_NOT_EXIST));

        if(depositOption.getReDepositProduct().getId() != depositProductId) throw new CustomException(ExceptionType.PRODUCT_OPTION_NOT_MATCH);

        // 옵션 기간 일치 여부 검사
        if (depositOption.getTerm() != accountCreationDto.getTerm())
            throw new CustomException(ExceptionType.TERM_MISMATCH);

        // 동일 상품 중복 가입 방지 (현재는 해당상품의 옵션으로 처리)
        Optional<ReDepositAccount> checkDupicate = reDepositAccountRepository
                .findByUserIdAndReDepositOptionsId(user.getId(), accountCreationDto.getDepositOptionId());
        if (checkDupicate.isPresent()){
            throw new CustomException(ExceptionType.ALREADY_EXISTS_REDEPOSIT_ACCOUNT);
        }

        // 정보 계산
        // 적용 금리
        BigDecimal depositRate = depositOption.getRate();
        if (user.getType() == UserType.TRAINEE) {
            depositRate = depositRate.add(depositOption.getPreferredRate1());
        } else if (user.getType() == UserType.FARMER) {
            depositRate = depositRate.add(depositOption.getPreferredRate2());
        }
        // 이자금액
        BigDecimal interest = accountCreationDto.getReDepositAmount().multiply(depositRate.divide(new BigDecimal("100.0"), 2));
        // 만기일
        LocalDate maturityDate = CalculateDate.GetMaturityDateWithTerm(LocalDate.now(), accountCreationDto.getTerm());
        // 계좌번호
        String accountNum = createAccountNum.getUsableAccountNum(2);
        // 총 합계
        BigDecimal totalAmount = accountCreationDto.getReDepositAmount().add(interest);

        log.info("이자율 : {}", depositRate);
        log.info("이자금액 : {}, ", interest);
        log.info("만기일 : {}, ", maturityDate);
        log.info("총 금액 : {}, ", totalAmount);


        // 지갑 잔액 확인 (블록체인 vs DB)
        FrDepositAccount frDepositAccount = frDepositAccountRepository.findByUser(user).orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

        // 지갑 잔액 확인
        if(frDepositAccount.getBalance().compareTo(accountCreationDto.getReDepositAmount()) < 0) throw new CustomException(ExceptionType.NOT_ENOUGH_MONEY);

        // 이자 지갑
        try {
            nhdcService.mintToInterestWallet(interest.toBigInteger(), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()));
        } catch (Exception e) {
            log.error("이자지갑에 이자 미리 넣기 실패 userId {}: {}",user.getId(), e.getMessage(),e);
            throw new CustomException(ExceptionType.FAILED_TO_TRANSFER_TOKEN);
        }

        // 예금 스마트 컨트랙트 계좌 개설
        BigInteger maturityPeriodInSeconds = CalculateDate.GetSecondsBetweenNowAndMaturityDate(accountCreationDto.getTerm(), LocalDate.now());

        String creationHash = "";
        try {
            creationHash = redepositBlockchainService.openAccount(
                    depositOption.getId(),
                    BigInteger.ZERO,
                    depositRate,
                    maturityPeriodInSeconds,
                    user.getWallet().getWalletPrivateKey()
            );
        } catch (Exception e) {
            log.error("Failed to create account for userID {}: {}",user.getId(),e.getMessage(),e);
            throw new CustomException(ExceptionType.FAILED_TO_CREATE_ACCOUNT);
        }

        log.info("Save : {}", accountCreationDto.getReDepositAmount());
        // 계좌정보 DB에 저장
        ReDepositAccount reDepositAccount = ReDepositAccount.builder()
                .user(user)
                .reDepositOptions(depositOption)
                .expirationAt(maturityDate)     // 만료일자
                .accountNum(accountNum)         // 계좌번호
                .amount(totalAmount)            // 금액 (원리금 + 이자)
                .interest(interest)             // 이자
                .balance(accountCreationDto.getReDepositAmount()) // 원리금
                .interestRate(depositRate)      // 이자율
                .contractAddress(redepositContractAddress)
                .build();

        ReDepositAccount saved = reDepositAccountRepository.save(reDepositAccount);

        // 이체 로직 및 거래내역 DB 저장
        transferService.transferFromWalletToReDeposit(frDepositAccount, saved, accountCreationDto.getReDepositAmount(), creationHash);

        try {
            drdcService.mint(user.getWallet().getWalletAddress(), totalAmount.divide(BigDecimal.TEN, 0, RoundingMode.DOWN).toBigInteger(), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
        } catch (Exception e) {
            TaskLog taskLog = TaskLog.builder()
                    .isSuccess(false)
                    .isProcessed(false)
                    .jobType(JopType.Blockchain)
                    .errorType(e.getClass().toString())
                    .errorMessage(e.getMessage())
                    .errorDetail(e.toString())
                    .build();
            taskLogRepository.save(taskLog);
        }

        notificationService.saveNotice(NotificationType.LOAN_EVENT, userId, BigDecimal.valueOf(0), 1, 2, "");


    }

    @Override
    public AccountDetailResDto getMyAccountDetail(int depositAccountId, int userId) {
        //유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 계좌
        ReDepositAccount account = reDepositAccountRepository.findById(depositAccountId)
                .orElseThrow(() ->new CustomException(ExceptionType.REDEPOSIT_ACCOUNT_NOT_EXIST));



        return AccountDetailResDto.builder()
                .name(account.getReDepositOptions().getReDepositProduct().getName())
                .amount(account.getBalance().longValue())
                .appliedInterateRate(account.getInterestRate().doubleValue())
                .expectedInterest(account.getInterest().longValue())
                .receivedAmount(account.getAmount().longValue())
                .openDate(account.getCreatedAt().toLocalDate())
                .expiredAt(account.getExpirationAt())
                .isActive(account.isActive())
                .build();
    }

    @Override
    public AccountHistoryResDto getMyAccountTransfer(int depositAccountId, int id) {

        // 계좌
        ReDepositAccount account = reDepositAccountRepository.findById(depositAccountId)
                .orElseThrow(() ->new CustomException(ExceptionType.REDEPOSIT_ACCOUNT_NOT_EXIST));

        List<ReDepositTransaction> depositTransactionList = redepositTransactionRepository.getDepositTransactionList(account);

        List<ReDepositTransactionDto> transactionDtoList = depositTransactionList.stream()
                .map(transaction -> {
                    ReDepositTransactionDto dto = ReDepositTransactionDto.builder()
                            .transactionAmount(transaction.getAmount().longValue())
                            .transactionBalance(transaction.getBalance().longValue())
                            .transactionDate(transaction.getCreatedAt().toLocalDate())
                            .type(transaction.getType())
                            .traderName(transaction.getOppositeName())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());

        return AccountHistoryResDto.builder()
                .name(account.getReDepositOptions().getReDepositProduct().getName())
                .accountNum(account.getAccountNum())
                .balance(account.getBalance().longValue())
                .reDepositTransactionList(transactionDtoList)
                .build();
    }

    // 정기예금 만기해지
    @Override
    @Transactional
    @Scheduled(cron = "0 20 2 * * *")
    public void expireReDepositAccount() {
        log.info("{}", LocalDate.now() );
        // 만기된 상품들 조회
        List<ReDepositAccount> expiredRedepositAccount = reDepositAccountRepository.expiredAccountList(LocalDate.now());

        for(ReDepositAccount reDepositAccount : expiredRedepositAccount){
            try{
                log.info("{}", reDepositAccount.getId());

                // 만기 예금 소유주
                User user = reDepositAccount.getUser();

                //그 사람의 자유입출금 계좌도 조회
                FrDepositAccount frDepositAccount = frDepositAccountRepository.findByUser(user).orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

                // 이체 금액 조회
                BigDecimal principal = reDepositAccount.getBalance();     // 원금
                BigDecimal interest = reDepositAccount.getInterest();   // 이자

                log.info("잔액 : {}", principal);
                log.info("원금 : {}", interest);

                // 블록체인 거래 해쉬값
                String principalHash = null;
                String interestHash = null;

                // 원금 반환
                try {
                    principalHash = redepositBlockchainService.returnPrincipal(reDepositAccount.getReDepositOptions().getId(), user.getWallet().getWalletPrivateKey());
                } catch (Exception e) {
                    log.error("원금 반환 실패 - userId: {}, savingAccountId: {}", user.getId(), reDepositAccount.getId());
                    log.error(e.getMessage(), e);
                }

                // 이자 지급
                try {
                    interestHash = nhdcService.transferFromInterestWallet(user.getWallet().getWalletAddress(), interest.toBigInteger(), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
                } catch (Exception e) {
                    log.error("이자 지급 실패 - userId: {}, savingAccountId: {}", user.getId(), reDepositAccount.getId());
                    log.error(e.getMessage(), e);
                }

                // DB 처리
                // 원금 거래
                frDepositAccount.transferIncreaseFrDepositBalance(principal);
                // 정기예금 상태변경 (원금 초기화, 비활성화, 삭제일자 추가)
                if(!reDepositAccount.withdrawOnMaturity()) throw new CustomException(ExceptionType.NOT_YET_EXPIRED);

                // 원금 거래내역 추가 (자유입출금 - 입급)
                FrDepositTransaction frDepositPrincipalTransaction = FrDepositTransaction.builder()
                        .frDepositAccount(frDepositAccount)
                        .type(0) // 입급
                        .oppositeAccount(reDepositAccount.getAccountNum())
                        .oppositeName(reDepositAccount.getAccountNum())
                        .tradeAmount(principal)
                        .remainingBalance(frDepositAccount.getBalance())
                        .transactionHash(principalHash)
                        .build();

                frDepositTransactionRepository.save(frDepositPrincipalTransaction);

                // 원금 거래내역 추가 (정기예금 - 출금)
                ReDepositTransaction reDepositPrincipalTransaction = ReDepositTransaction.builder()
                        .reDepositAccount(reDepositAccount)
                        .type(1) // 출금
                        .amount(principal)
                        .oppositeName(user.getName())
                        .balance(reDepositAccount.getBalance())
                        .transactionHash(principalHash)
                        .build();

                redepositTransactionRepository.save(reDepositPrincipalTransaction);

                // 이자 거래
                frDepositAccount.transferIncreaseFrDepositBalance(interest);
                if(!reDepositAccount.withdrawInterestOnMaturity()) throw new CustomException(ExceptionType.NOT_YET_EXPIRED);

                // 이자 거래내역 추가 (자유입출금 - 입급)
                FrDepositTransaction frDepositInterestTransaction = FrDepositTransaction.builder()
                        .frDepositAccount(frDepositAccount)
                        .type(0)
                        .oppositeAccount(reDepositAccount.getAccountNum())
                        .oppositeName("만기이자")
                        .tradeAmount(interest)
                        .remainingBalance(frDepositAccount.getBalance())
                        .transactionHash(interestHash)
                        .build();

                frDepositTransactionRepository.save(frDepositInterestTransaction);

                // 원금 거래내역 추가 (정기예금 - 출금)
                ReDepositTransaction reDepositInterestTransaction = ReDepositTransaction.builder()
                        .reDepositAccount(reDepositAccount)
                        .type(1) // 출금
                        .amount(principal)
                        .oppositeName(user.getName())
                        .balance(reDepositAccount.getBalance())
                        .transactionHash(principalHash)
                        .build();

                redepositTransactionRepository.save(reDepositInterestTransaction);

            }catch (CustomException e) {
                log.error("reDepositAccount: {} 만기 처리 실패 에러메시지 : {}", reDepositAccount.getId(), e.getMessage());
            } catch (Exception e) {
                // 예상치 못한 다른 예외들 처리
                log.error("예외처리한 예외말고 다른 예외 발생", e);
            }
        }

    }

}

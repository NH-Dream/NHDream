package com.ssafy.nhdream.domain.saving.service;

import com.ssafy.nhdream.common.drdc.DRDCService;
import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.common.utils.CalculateDate;
import com.ssafy.nhdream.common.utils.CalculateInterest;
import com.ssafy.nhdream.common.utils.CreateAccountNum;
import com.ssafy.nhdream.domain.admin.repository.TaskLogRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.AutomaticTransferTaskRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositTransactionRepository;
import com.ssafy.nhdream.domain.saving.dto.*;
import com.ssafy.nhdream.domain.saving.repository.SavingAccountRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingOptionRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingProductRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingTransactionRepository;
import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.domain.sse.service.NotificationService;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.admin.JopType;
import com.ssafy.nhdream.entity.admin.TaskLog;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import com.ssafy.nhdream.entity.saving.SavingOptions;
import com.ssafy.nhdream.entity.saving.SavingProduct;
import com.ssafy.nhdream.entity.saving.SavingTransaction;
import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import com.ssafy.nhdream.entity.user.User;
import com.ssafy.nhdream.entity.user.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingServiceImpl implements SavingService {
    private final UserRepository userRepository;
    private final SavingProductRepository savingProductRepository;
    private final SavingOptionRepository savingOptionRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final SavingTransactionRepository savingTransactionRepository;
    private final SavingBlockchainService savingBlockchainService;
    private final NHDCService nhdcService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CreateAccountNum createAccountNum;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final FrDepositTransactionRepository frDepositTransactionRepository;
    private final AutomaticTransferTaskRepository automaticTransferTaskRepository;
    private final TaskLogRepository taskLogRepository;
    private final DRDCService drdcService;

    // SSE 설정
    private final NotificationService notificationService;

    @Value("${NHDC.privateKey}")
    private String serverPrivateKey;

    @Value("${saving.contractAddress}")
    private String savingContractAddress;

    @Override
    @Transactional(readOnly = true)
    public List<SavingProductListResDto> getSavingProductList() {
        return savingProductRepository.findSavingProductsWithTerm24();
    }

    //적금상품생성
    @Transactional
    @Override
    public int createSavingProduct(SavingProductCreationReqDto savingProductCreationReqDto) {
        SavingProduct savingProduct = SavingProduct.builder()
                .name(savingProductCreationReqDto.getName())
                .maxMonthlyLimit(savingProductCreationReqDto.getMaxMonthlyLimit())
                .build();

        savingProduct = savingProductRepository.save(savingProduct);

        for (SavingOptionCreationReqDto optionDto : savingProductCreationReqDto.getOptions()) {
            SavingOptions option = SavingOptions.builder()
                    .savingProduct(savingProduct)
                    .rate(optionDto.getOptionRate())
                    .preferredRate1(optionDto.getOptionPreferredRate1())
                    .preferredRate2(optionDto.getOptionPreferredRate2())
                    .term(optionDto.getTerm())
                    .build();
            savingOptionRepository.save(option);
        }
        return savingProduct.getId();
    }

    //적금상품상세조회
    @Transactional(readOnly = true)
    @Override
    public SavingProductDetailResDto getSavingProductDetail(int savingProductId) {
        SavingProduct savingProduct = savingProductRepository.findById(savingProductId)
                .orElseThrow(() -> new CustomException(ExceptionType.SAVINGPRODUCT_NOT_EXIST));
        SavingProductDetailResDto.SavingProductDetailResDtoBuilder builder = SavingProductDetailResDto.builder()
                .id(savingProduct.getId())
                .maxMonthlyLimit(savingProduct.getMaxMonthlyLimit())
                .name(savingProduct.getName());
        BigDecimal maxRate = BigDecimal.ZERO;
        BigDecimal minRate = BigDecimal.ZERO;
        List<SavingOptions> savingOptionsList = savingProduct.getSavingOptions();
        for (SavingOptions savingOptions : savingOptionsList) {
            BigDecimal graduateRate = savingOptions.getRate().add(savingOptions.getPreferredRate1());
            BigDecimal farmerRate = savingOptions.getRate().add(savingOptions.getPreferredRate2());
            if (savingOptions.getTerm() == 6) {
                minRate = graduateRate.min(farmerRate);
                builder.graduateRate6Months(graduateRate)
                        .farmerRate6Months(farmerRate)
                        .option6Id(savingOptions.getId());
            } else if (savingOptions.getTerm() == 12) {
                builder.graduateRate12Months(graduateRate)
                        .farmerRate12Months(farmerRate)
                        .option12Id(savingOptions.getId());
            } else if (savingOptions.getTerm() == 24) {
                maxRate = graduateRate.max(farmerRate);
                builder.graduateRate24Months(graduateRate)
                        .farmerRate24Months(farmerRate)
                        .option24Id(savingOptions.getId());
            }
        }
        return builder.minRate(minRate)
                .maxRate(maxRate)
                .build();
    }

    //적금계좌생성
    @Transactional
    @Override
    public int createSavingAccount(SavingAccountCreationReqDto savingAccountCreationReqDto, int userId) {
        //유저
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));



        //비밀번호가 다를 때
        if (!bCryptPasswordEncoder.matches(savingAccountCreationReqDto.getPassword(), user.getWalletPassword())) {
            throw new CustomException(ExceptionType.WALLETPASSWORD_NOT_MATCH);
        }

        //적금옵션
        SavingOptions savingOptions = savingOptionRepository.findById(savingAccountCreationReqDto.getSavingOptionId())
                .orElseThrow(() -> new CustomException(ExceptionType.SAVINGOPTION_NOT_EXIST));
        //적금상품
        SavingProduct savingProduct = savingOptions.getSavingProduct();
        //이미 해당상품의 계좌가 있을 때
        if (savingAccountRepository.existsByUserIdAndSavingOptions_SavingProduct_Id(userId, savingProduct.getId())) {

            throw new CustomException(ExceptionType.ALREADY_EXISTS_SAVING_ACCOUNT);
        }



        //입력받은 기간과 옵션의 기간이 다를 경우
        if (savingOptions.getTerm() != savingAccountCreationReqDto.getTerm()) {
            throw new CustomException(ExceptionType.TERM_MISMATCH);
        }
        BigDecimal savingRate = savingOptions.getRate();
        if (user.getType() == UserType.TRAINEE) {
            savingRate = savingRate.add(savingOptions.getPreferredRate1());
        } else if (user.getType() == UserType.FARMER) {
            savingRate = savingRate.add(savingOptions.getPreferredRate2());
        }


        //시작일
        LocalDate firstDepositDate = CalculateDate.findNextPaymentDate(savingAccountCreationReqDto.getDepositDate());
        //만기일
        LocalDate maturityDate = CalculateDate.GetMaturityDateWithTerm(firstDepositDate, savingAccountCreationReqDto.getTerm());

        //납입액이 최대한도보다 클경우
        if (savingAccountCreationReqDto.getMonthlyAmount().compareTo(savingProduct.getMaxMonthlyLimit()) > 0) {
            throw new CustomException(ExceptionType.EXCEED_MAX_MONTHLY_LIMIT);
        }

        //최대이자액 이자지갑에 저장
        BigDecimal maxInterest = CalculateInterest.calculateTotalSavingInterest(savingAccountCreationReqDto.getMonthlyAmount(), savingRate, savingAccountCreationReqDto.getTerm());

        //만기일과 지금까지의 시간
        BigInteger maturityPeriodInSeconds = CalculateDate.GetSecondsBetweenNowAndMaturityDate(savingAccountCreationReqDto.getTerm(), firstDepositDate);
        //블록체인부분
        try {
            nhdcService.mintToInterestWallet(maxInterest.toBigInteger(), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()));
        } catch (Exception e) {
            log.error("이자지갑에 이자 미리 넣기 실패 userId {}: {}",user.getId(), e.getMessage(),e);
            throw new CustomException(ExceptionType.FAILED_TO_TRANSFER_TOKEN);
        }

        try {
        savingBlockchainService.openAccount(savingOptions.getId(), BigInteger.ZERO, savingRate.multiply(BigDecimal.valueOf(100)), maturityPeriodInSeconds ,user.getWallet().getWalletPrivateKey());
        } catch (Exception e) {
            log.error("Failed to create account for userID {}: {}",user.getId(),e.getMessage(),e);
            throw new CustomException(ExceptionType.FAILED_TO_CREATE_SAVINGACCOUNT);
        }
        String accountNum = createAccountNum.getUsableAccountNum(3);
        int installmentCount = 0;
        BigDecimal balance = BigDecimal.ZERO;

        SavingAccount newSavingAccount = SavingAccount.builder()
                .user(user)
                .savingOptions(savingOptions)
                .expireAt(maturityDate)
                .interestRate(savingRate)
                .contractAddress(savingContractAddress)
                .accountNum(accountNum)
                .installmentCount(installmentCount)
                .nextDepositDate(firstDepositDate)
                .balance(balance)
                .depositDay(savingAccountCreationReqDto.getDepositDate())
                .monthlyAmount(savingAccountCreationReqDto.getMonthlyAmount())
                .nextInterestDate(CalculateDate.nextValidDate(firstDepositDate.plusMonths(1),savingAccountCreationReqDto.getDepositDate()))
                .startDate(firstDepositDate)
                .build();

        SavingAccount savedSavingAccount = savingAccountRepository.save(newSavingAccount);

        //자동이체에 추가 돈뺄 지갑 지갑업으면 예외처리
        FrDepositAccount frDepositAccount = frDepositAccountRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

        AutomaticTransferTask automaticTransferTask = AutomaticTransferTask.builder()
                .user(user)
                .senderWalletAccount(frDepositAccount)
                .recipientAccount(savedSavingAccount.getAccountNum())
                .recipientName(savingProduct.getName())
                .recurringAmount(savingAccountCreationReqDto.getMonthlyAmount())
                .transferDay(savingAccountCreationReqDto.getDepositDate())
                .nextScheduleTime(firstDepositDate)
                .startedAt(firstDepositDate)
                .expiredAt(savedSavingAccount.getExpireAt())
                .term(savingAccountCreationReqDto.getTerm())
                .type(1)
                .build();


        automaticTransferTaskRepository.save(automaticTransferTask);

        try {
            drdcService.mint(user.getWallet().getWalletAddress(), savingAccountCreationReqDto.getMonthlyAmount().divide(BigDecimal.TEN, 0, RoundingMode.DOWN).toBigInteger(), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
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

        // sse
        notificationService.saveNotice(NotificationType.LOAN_EVENT, userId, BigDecimal.valueOf(0), 2, 2, "");

        return savedSavingAccount.getId();

    }

    //적금계좌 거래 내역 조회
    @Override
    @Transactional(readOnly = true)
    public SavingHistoryResDto getSavingHistory(int savingAccountId, int userId, int page, int size) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
        SavingAccount savingAccount = savingAccountRepository.findById(savingAccountId)
                .orElseThrow(() -> new CustomException(ExceptionType.SAVINGACCOUNT_NOT_EXIST));
        SavingOptions savingOptions = savingAccount.getSavingOptions();
        SavingProduct savingProduct = savingOptions.getSavingProduct();
        //로그인유저와 계좌주인이 다를 때
        if (!savingAccount.getUser().equals(loginUser)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED_SAVINGACCOUNT_ACCESS);
        }
        Page<SavingTransaction> savingTransactions = savingTransactionRepository.getSavingTransactionList(savingAccount, PageRequest.of(page, size));

        List<SavingTransactionResDto> savingTransactionResDtoList= savingTransactions.getContent().stream().map(savingTransaction -> SavingTransactionResDto.builder()
                .id(savingTransaction.getId())
                .type(savingTransaction.getType())
                .oppositeAccount(savingTransaction.getOppositeAccount())
                .oppositeName(savingTransaction.getOppositeName())
                .remainingBalance(savingTransaction.getRemainingBalance())
                .transactionHash(savingTransaction.getTransactionHash())
                .tradeAmount(savingTransaction.getAmount())
                .tradedAt(savingTransaction.getCreatedAt().toLocalDate())
                .build()
        ).collect(Collectors.toList());

        return SavingHistoryResDto.builder()
                .name(savingProduct.getName())
                .accountNum(savingAccount.getAccountNum())
                .balance(savingAccount.getBalance().setScale(0, RoundingMode.DOWN).longValueExact())
                .savingTransactionList(savingTransactionResDtoList)
                .build();
    }


    //적금계좌상세조회
    @Override
    @Transactional(readOnly = true)
    public SavingAccountDetailResDto getSavingAccountDetail(int savingAccountId, int userId) {
        //유저가 없을 때
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
        //계좌가 없을 때
        SavingAccount savingAccount = savingAccountRepository.findById(savingAccountId)
                .orElseThrow(() -> new CustomException(ExceptionType.SAVINGACCOUNT_NOT_EXIST));
        //계좌에서 옵션 들고오기
        SavingOptions savingOptions = savingAccount.getSavingOptions();
        //옵션에서 상품 들고오기
        SavingProduct savingProduct = savingOptions.getSavingProduct();

        int interestRate = savingAccount.getInterestRate()
                .setScale(2, RoundingMode.DOWN)
                .multiply(new BigDecimal(100))
                .intValueExact();

        //만기여부 만기됐으면 type1로
        int type = 0;
        if (!savingAccount.isActive()) {
            type =1;
        }

        return SavingAccountDetailResDto.builder()
                .name(savingProduct.getName())
                .balance(savingAccount.getBalance().setScale(0, RoundingMode.DOWN).longValueExact())
                .accountNum(savingAccount.getAccountNum())
                .interestRate(interestRate)
                .joinDate(savingAccount.getStartDate())
                .expiredAt(savingAccount.getExpireAt())
                .monthlyAmount(savingAccount.getMonthlyAmount().setScale(0, RoundingMode.DOWN).longValueExact())
                .type(type)
                .build();
    }


    //적금만기해지
    @Override
    @Transactional
    @Scheduled(cron = "0 20 2 * * *")
    public void expireSavingAccount() {
        //만기된 상품들 조회
        List<SavingAccount> expiredSavingAccount = savingAccountRepository.expiredAccountList(LocalDate.now());
        for (SavingAccount savingAccount : expiredSavingAccount) {
            try {


                //계좌주인찾고
                User user = savingAccount.getUser();
                //그 계좌주인의 자유입출금 계좌도 조회
                FrDepositAccount frDepositAccount = frDepositAccountRepository.findByUser(user).orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

                BigDecimal principal = savingAccount.getBalance();
                BigDecimal interest = savingAccount.getInterest();

                String principalHash = null;
                String interestHash = null;
                //블록체인으로 해지
                try {
                    principalHash = savingBlockchainService.returnPrincipal(savingAccount.getSavingOptions().getId(), user.getWallet().getWalletPrivateKey());
                } catch (Exception e) {
                    log.error("블록체인 해지 실패 userId: {}, savingAccountId: {}", user.getId(), savingAccount.getId());
                    log.error(e.getMessage(), e);
                }
                //블록체인으로 이자지급
                try {
                    interestHash = nhdcService.transferFromInterestWallet(user.getWallet().getWalletAddress(), interest.toBigInteger(), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
                } catch (Exception e) {
                    log.error("블록체인 이자 지급 실패 userId: {}, savingAccountId: {}", user.getId(), savingAccount.getId());
                    log.error(e.getMessage(), e);
                }

                //다시 유저의 자유입출금계좌(지갑)으로 전송, 각각 잔액 처리 해주기

                frDepositAccount.transferIncreaseFrDepositBalance(principal);
                savingAccount.withdrawOnMaturity();


                //해당 거래내역 자유입출금, 적금 각각 생성

                FrDepositTransaction frDepositPrincipalTransaction = FrDepositTransaction.builder()
                        .frDepositAccount(frDepositAccount)
                        .type(0)
                        .oppositeAccount(savingAccount.getAccountNum())
                        .oppositeName(savingAccount.getSavingOptions().getSavingProduct().getName())
                        .tradeAmount(principal)
                        .remainingBalance(frDepositAccount.getBalance())
                        .transactionHash(principalHash)
                        .build();

                frDepositTransactionRepository.save(frDepositPrincipalTransaction);


                SavingTransaction savingPrincipalTransaction = SavingTransaction.builder()
                        .savingAccount(savingAccount)
                        .type(1)
                        .amount(principal)
                        .oppositeAccount(savingAccount.getContractAddress())
                        .oppositeName(user.getName())
                        .remainingBalance(savingAccount.getBalance())
                        .transactionHash(principalHash)
                        .build();

                savingTransactionRepository.save(savingPrincipalTransaction);


                //이자지급하기 이자지급할때 이름 만기이자로 하드코딩

                frDepositAccount.transferIncreaseFrDepositBalance(interest);
                savingAccount.withdrawInterestOnMaturity();

                //이자거래내역 적기
                FrDepositTransaction frDepositInterestTransaction = FrDepositTransaction.builder()
                        .frDepositAccount(frDepositAccount)
                        .type(0)
                        .oppositeAccount(savingAccount.getAccountNum())
                        .oppositeName("만기이자")
                        .tradeAmount(interest)
                        .remainingBalance(frDepositAccount.getBalance())
                        .transactionHash(interestHash)
                        .build();

                frDepositTransactionRepository.save(frDepositInterestTransaction);

                SavingTransaction savingInterestTransaction = SavingTransaction.builder()
                        .savingAccount(savingAccount)
                        .type(1)
                        .amount(interest)
                        .oppositeAccount(savingAccount.getContractAddress())
                        .oppositeName(user.getName())
                        .remainingBalance(savingAccount.getBalance())
                        .transactionHash(interestHash)
                        .build();

                savingTransactionRepository.save(savingInterestTransaction);
            } catch (CustomException e) {
                log.error("savingAccountId: {} 만기 처리 실패 에러메시지 : {}", savingAccount.getId(), e.getMessage());
            } catch (Exception e) {
                // 예상치 못한 다른 예외들 처리
                log.error("예외처리한 예외말고 다른 예외 발생", e);
            }
        }

    }

    //달마다 이자 넣는 함수
    @Override
    @Transactional
    @Scheduled(cron = "0 20 1 * * *")

    public void interestToSavingAccount() {
        List<SavingAccount> accountsToReceiveInterest = savingAccountRepository.accountsToReceiveInterest(LocalDate.now());

        for (SavingAccount savingAccount : accountsToReceiveInterest) {
            try {


                BigDecimal nowPrincipal = savingAccount.getBalance();
                BigDecimal monthlyInterestRate = savingAccount.getInterestRate().divide(BigDecimal.valueOf(12)).setScale(4, RoundingMode.DOWN);
                BigDecimal monthlyInterest = nowPrincipal.multiply(monthlyInterestRate).setScale(0, RoundingMode.CEILING);

                LocalDate nextInterestDate = CalculateDate.nextValidDate(LocalDate.now().plusMonths(1), savingAccount.getDepositDay());

                //이자지급날이 만기일 후라면 더이상 이자는 필요 없으므로 null로 바꿈
                if (nextInterestDate.isAfter(savingAccount.getExpireAt())) {
                    nextInterestDate = null;
                }
                savingAccount.updateInterestAndNextInterestDate(monthlyInterest, nextInterestDate);
            } catch (CustomException e) {
                log.error("savingAccountId: {} 이자 지급실패 에러메시지 : {}", savingAccount.getId(), e.getMessage());
            } catch (Exception e) {
                // 예상치 못한 다른 예외들 처리
                log.error("예외처리한 예외말고 다른 예외 발생", e);
            }
        }
    }


}

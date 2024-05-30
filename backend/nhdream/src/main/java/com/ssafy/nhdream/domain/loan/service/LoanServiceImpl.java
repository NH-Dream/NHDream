package com.ssafy.nhdream.domain.loan.service;

import com.ssafy.nhdream.common.drdc.DRDCService;
import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.s3.AwsS3Service;
import com.ssafy.nhdream.common.utils.CalculateDate;
import com.ssafy.nhdream.common.utils.CreateAccountNum;
import com.ssafy.nhdream.domain.admin.repository.TaskLogRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.AutomaticTransferTaskRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositTransactionRepository;
import com.ssafy.nhdream.domain.loan.dto.*;
import com.ssafy.nhdream.domain.loan.repository.*;
import com.ssafy.nhdream.domain.redeposit.repository.RedepositTransactionRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingTransactionRepository;
import com.ssafy.nhdream.domain.user.dto.TransactionDto;
import com.ssafy.nhdream.domain.user.dto.TransactionListResDto;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.admin.JopType;
import com.ssafy.nhdream.entity.admin.TaskLog;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.loan.*;
import com.ssafy.nhdream.entity.redeposit.ReDepositTransaction;
import com.ssafy.nhdream.entity.saving.SavingTransaction;
import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import com.ssafy.nhdream.entity.user.User;
import com.ssafy.nhdream.entity.user.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanAccountRepository loanAccountRepository;
    private final LoanOptionRepository loanOptionRepository;
    private final LoanProductRepository loanProductRepository;
    private final LoanApprovalRepository loanApprovalRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final CreateAccountNum createAccountNum;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final AutomaticTransferTaskRepository automaticTransferTaskRepository;
    private final LoanBlockchainService loanBlockchainService;
    private final DRDCService drdcService;
    private final TaskLogRepository taskLogRepository;

    // 전체 목록 조회
    private final LoanTransactionRepository loanTransactionRepository;
    private final SavingTransactionRepository savingTransactionRepository;
    private final RedepositTransactionRepository redepositTransactionRepository;
    private final FrDepositTransactionRepository frDepositTransactionRepository;

    @Value("${NHDC.privateKey}")
    private String serverPrivateKey;

    @Value("${loan.contractAddress}")
    private String loanContractAddress;

    @Override
    @Transactional(readOnly = true)
    public LoanListResDto getLoanList() {

        List<LoanProduct> loans = loanProductRepository.findAllByLoanProduct();
        List<LoanListDto> loanList = new ArrayList<>();

        for (LoanProduct loanProduct : loans) {
            LoanListDto loanListDto = LoanListDto.builder()
                    .id(loanProduct.getId())
                    .name(loanProduct.getName())
                    .minRate(loanProduct.getMinRate())
                    .maxRate(loanProduct.getPreferredRate2())
                    .amountRange(loanProduct.getAmountRange().setScale(0, BigDecimal.ROUND_FLOOR))
                    .build();

            loanList.add(loanListDto);
        }

        return LoanListResDto.builder()
                .loanListDtos(loanList)
                .build();
    }

    @Override
    public LoanResDto getLoanAccountDetail(int id) {

        LoanAccount loanAccount = loanAccountRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.LOANACCOUNT_NOT_EXIST));

        return LoanResDto.builder()
                .principal(loanAccount.getPrincipal().setScale(0, BigDecimal.ROUND_FLOOR))
                .interest(loanAccount.getInterest().setScale(0, BigDecimal.ROUND_FLOOR))
                .createdAt(loanAccount.getStartedAt())
                .expirationAt(loanAccount.getExpirationAt())
                .outstanding(loanAccount.getOutstandingPrincipal().setScale(0, BigDecimal.ROUND_FLOOR))
                .round(loanAccount.getRound())
                .paymentMethod(loanAccount.getPaymentMethod())
                .paymentDate(loanAccount.getPaymentDate())
                .build();
    }

    @Override
    public int createLoanOption(LoanOptionReqDto loanOptionReqDto) {

        LoanProduct loanProduct = loanProductRepository.findById(loanOptionReqDto.getLoanProductId()).orElseThrow(() -> new CustomException((ExceptionType.LOANPRODUCT_NOT_EXIST)));

        int userId = loanOptionReqDto.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        UserType userType = user.getType();

        int term = loanOptionReqDto.getTerm();

        BigDecimal rate;

        if (userType == UserType.UNVERIFIED) { // 회원타입(직업)이 아무것도 없다면
            rate = loanProduct.getMinRate();
        } else if (userType == UserType.TRAINEE) { // 회원타입(직업)이 수료자라면
            if (term == 6) {
                rate = loanProduct.getPreferredRate2();
            } else if (term == 12) {
                rate = loanProduct.getPreferredRate2().subtract(BigDecimal.valueOf(0.3));
            } else {
                rate = loanProduct.getPreferredRate2().subtract(BigDecimal.valueOf(0.6));
            }
        } else if (userType == UserType.FARMER) { // 회원타입(직업)이 귀농자라면
            if (term == 6) {
                rate = loanProduct.getMinRate().add(BigDecimal.valueOf(0.6));
            } else if (term == 12) {
                rate = loanProduct.getMinRate().add(BigDecimal.valueOf(0.3));
            } else {
                rate = loanProduct.getMinRate();
            }
        } else {
            throw new CustomException(ExceptionType.USER_NOT_EXIST);
        }

        LoanOptions loanOptions = LoanOptions.builder()
                .loanProduct(loanProduct)
                .rate(rate)
                .amount(loanOptionReqDto.getAmount())
                .paymentDate(loanOptionReqDto.getPaymentDate())
                .paymentMethod(loanOptionReqDto.getPaymentMethod())
                .term(loanOptionReqDto.getTerm())
                .user(user)
                .build();
        loanOptionRepository.save(loanOptions);

        return loanOptions.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public LoanOptionDetailResDto getLoanOptionDetail(int id) {
        LoanOptions loanOptions = loanOptionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.LOANOPTIONS_NOT_EXIST));

        LoanOptionDetailResDto loanOptionDetailResDto = LoanOptionDetailResDto.builder()
                .id(loanOptions.getId())
                .name(loanOptions.getLoanProduct().getName())
                .term(loanOptions.getTerm())
                .amount(loanOptions.getAmount().setScale(0, BigDecimal.ROUND_FLOOR))
                .rate(loanOptions.getRate())
                .paymentMethod(loanOptions.getPaymentMethod())
                .paymentDate(loanOptions.getPaymentDate())
                .build();

        return loanOptionDetailResDto;
    }

    @Override
    public int createLoanApproval(int userId, int loanOptionId, List<MultipartFile> files) {

        if (files.size() != 5) {
            throw new CustomException(ExceptionType.USER_NOT_EXIST);
        }

        LoanApproval loanApproval = LoanApproval.builder()
                .user(userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST)))
                .loanOptions(loanOptionRepository.findById(loanOptionId).orElseThrow(() -> new CustomException(ExceptionType.LOANOPTIONS_NOT_EXIST)))
                .approval(ApprovalStatus.PENDING)
                .identityUrl(awsS3Service.uploadFile(files.get(0)))
                .farmlandAccessUrl(awsS3Service.uploadFile(files.get(1)))
                .incomeCertificateUrl(awsS3Service.uploadFile(files.get(2)))
                .nationalTaxCertificateUrl(awsS3Service.uploadFile(files.get(3)))
                .localTaxCertificateUrl(awsS3Service.uploadFile(files.get(4)))
                .build();
        loanApprovalRepository.save(loanApproval);

        return loanApproval.getId();
    }

    @Override
    @Transactional
    public int createLoanAccount(int id) {
        // 받은 심사 id를 통해 심사 entity 불러오기
        LoanApproval loanApproval = loanApprovalRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.LOANAPPROVAL_NOT_EXIST));

        if (loanApproval.getApproval() != ApprovalStatus.APPROVED) { // 대출 심사가 승인이 아니라면 컷
            // 나중에 머지 전에 바꿔줘 응애
            throw new CustomException(ExceptionType.LOANAPPROVAL_NOT_EXIST);
        }

        LoanAccount loanAccount;

        LoanOptions loanOptions = loanApproval.getLoanOptions();
        LoanProduct loanProduct = loanOptions.getLoanProduct();
        User user = loanApproval.getUser();

        // 트랜잭션주소
        String transactionHash = null;

        // 계좌번호 만들기
        String accountNum = createAccountNum.getUsableAccountNum(1);

        BigDecimal principal = loanOptions.getAmount();
        BigDecimal rate = loanOptions.getRate();

        //달마다 갚아야할 원금
        BigDecimal monthlyPrincipal = principal.divide(BigDecimal.valueOf(loanOptions.getTerm()), 0, RoundingMode.DOWN);
        // 이자 계산 = 원금 * 이자율 / 100 -> 소수점 내림
        BigDecimal interest = principal.multiply(rate).divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN);

        //달마다 갚아야할 이자
        BigDecimal monthlyInterest = interest.divide(BigDecimal.valueOf(loanOptions.getTerm()), 0, RoundingMode.DOWN);

        //달마다 갚을 돈
        BigDecimal monthlyPayment = monthlyInterest.add(monthlyPrincipal);

        BigDecimal amount = principal.add(interest);

        // 대출 시작날짜, 종료날짜 계산해서 build에 넣기
        LocalDate startedAt = CalculateDate.findNextPaymentDate(loanOptions.getPaymentDate());
        LocalDate expirationAt = CalculateDate.GetMaturityDateWithTerm(startedAt, loanOptions.getTerm());

        
        // 블록체인 코드가 들어갈 부분 contractAddress 생성
        // **********************************
        // **********************************
        try {
            transactionHash = loanBlockchainService.issueLoan(loanProduct.getId(), user.getWallet().getWalletAddress(), principal, interest, rate, loanOptions.getTerm(), CalculateDate.GetSecondsBetweenNowAndMaturityDate(loanOptions.getTerm(), CalculateDate.findNextPaymentDate(loanOptions.getPaymentDate())), monthlyPayment);
        } catch (Exception e) {
            log.error("대출 생성 실패 userId: {}, loanOptionId: {}", user.getId(),loanOptions.getId());
            log.error("에러메세지 : {}", e.getMessage());
            throw new CustomException(ExceptionType.FAILED_TO_CREATE_LOAN);
        }


        // 계좌번호, 대출 옵션, 유저, 대출 상품명, 시작일자, 만료일자,
        // 원금, 원금잔액, 이자, 이자잔액, 이자율, 원리금, 총잔액, 결제방법, 결제일자
        // contractAddress 추가 필요 -> 블록체인 추가되면 넣어야 할듯
        loanAccount = LoanAccount.builder()
                .accountNum(accountNum)
                .loanOptions(loanOptions)
                .user(user)
                .productName(loanOptions.getLoanProduct().getName())
                .principal(loanOptions.getAmount())
                .outstandingPrincipal(loanOptions.getAmount())
                .interest(interest)
                .outstandingInterest(interest)
                .interestRate(rate)
                .amount(amount)
                .balance(amount)
                .paymentMethod(loanOptions.getPaymentMethod())
                .paymentDate(loanOptions.getPaymentDate())
                .startedAt(startedAt)
                .expirationAt(expirationAt)
                .round(0)
                .state(0)
                .term(loanOptions.getTerm())
                .loanApproval(loanApproval)
                .contractAddress(loanContractAddress)
                .build();


        loanAccountRepository.save(loanAccount);

        createAutoTransferLoan(loanAccount);
        try {
            drdcService.mint(user.getWallet().getWalletAddress(), amount.divide(BigDecimal.TEN, 0, RoundingMode.DOWN).toBigInteger(), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
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


        return loanAccount.getId();
    }

    // 대출 상환 시 transaction Table 생성 Transfer로 옮김
//    public void createLoanTansaction(int accountId) {
//        LoanAccount loanAccount = loanAccountRepository.findById(accountId).orElseThrow(() -> new CustomException(ExceptionType.LOANACCOUNT_NOT_EXIST));
//        User user = userRepository.findById(loanAccount.getUser().getId()).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
//        FrDepositAccount frDepositAccount = frDepositAccountRepository.findByUser(user).orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));
//
//        // 대출이 완료 되었는지 확인
//        if (loanAccount.getState() == 1) {
//            throw new CustomException(ExceptionType.LOAN_REPAYMENT_ALREADY);
//        }
//
//        // 갚을 이자
//        BigDecimal interest = loanAccount.getInterest().divide(BigDecimal.valueOf(loanAccount.getTerm()),0,RoundingMode.DOWN);
//
//        // 갚을 원금
//        BigDecimal principal = loanAccount.getPrincipal().divide(BigDecimal.valueOf(loanAccount.getTerm()));
//
//        // 갚을 돈 (이번달에 나갈 돈)
//        BigDecimal amount = interest.add(principal);
//
//        // 사용자의 자유입출금 계좌의 잔액이 갚을 돈 보다 많은지 확인
//        if (frDepositAccount.getBalance().compareTo(amount) < 0) {
//            // 예외 이름 넣기
//            throw new CustomException(ExceptionType.NOT_REMAINING_MONEY);
//        }
//
//        // 블록체인 코드가 들어갈 부분
//        // ******************************************************
//        // ******************************************************
//
//
//        // 블록체인 거래 후 loanAccount 수정
//        // 수정된 원금 잔액, 이자 잔액, 원리금 잔액, 납입 회차+1
//        BigDecimal newInterest = loanAccount.getOutstandingInterest().subtract(interest);
//        BigDecimal newPrincipal = loanAccount.getOutstandingPrincipal().subtract(principal);
//        BigDecimal newAmount = loanAccount.getBalance().subtract(amount);
//
//        loanAccountRepository.updateBalance(loanAccount.getId(), newInterest, newPrincipal, newAmount);
//
//
//        // loanAccount 수정 후 LoanTransaction 생성
//        LoanTransaction loanTransaction = LoanTransaction.builder()
//                .type(0)
//                .interest(interest)
//                .principal(principal)
//                .oppositeAccount(frDepositAccount.getAccountNum())
//                .oppositeName(loanAccount.getProductName())
//                .outstanding(newAmount)
//                .loanAccount(loanAccount)
//                .build();
//
//        loanTransactionRepository.save(loanTransaction);
//
//        // 납입회차와 대출기간이 같아졌는지 확인
//        // 같다면 남은 잔액이 0원인지 확인
//        if (loanAccount.getRound() == loanAccount.getTerm()) {
//            if (loanAccount.getBalance().compareTo(BigDecimal.valueOf(0)) == 0) {
//                // 대출 상환 완료 (대출 통장 상태 1로 변경)
//                loanAccountRepository.finishLoan(loanAccount.getId());
//            }
//
//        }
//
//
//    }

    @Transactional
    public void createAutoTransferLoan(LoanAccount loanAccount) {

        //자동이체에 추가 돈뺄 지갑 지갑업으면 예외처리
        FrDepositAccount frDepositAccount = frDepositAccountRepository.findByUser(loanAccount.getUser())
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

        BigDecimal interest = loanAccount.getInterest().divide(BigDecimal.valueOf(loanAccount.getTerm()),0, RoundingMode.DOWN);

        // 갚을 원금
        BigDecimal principal = loanAccount.getPrincipal().divide(BigDecimal.valueOf(loanAccount.getTerm()),0, RoundingMode.DOWN);

        // 갚을 돈 (이번달에 나갈 돈)
        BigDecimal monthlyAmount = interest.add(principal);

        //자동이체에 등록(달마다 자동이체 기준으로 스케쥴러 돌리기 위함)
        AutomaticTransferTask automaticTransferTaskFromLoan = AutomaticTransferTask.builder()
                .user(loanAccount.getUser())
                .senderWalletAccount(frDepositAccount)
                .recipientAccount(loanAccount.getAccountNum())
                .recipientName(loanAccount.getProductName())
                .recurringAmount(monthlyAmount)
                .transferDay(loanAccount.getPaymentDate())
                .nextScheduleTime(CalculateDate.findNextPaymentDate(loanAccount.getPaymentDate()))
                .startedAt(CalculateDate.findNextPaymentDate(loanAccount.getPaymentDate()))
                .expiredAt(loanAccount.getExpirationAt())
                .term(loanAccount.getTerm())
                .type(2)
                .build();
        automaticTransferTaskRepository.save(automaticTransferTaskFromLoan);
    }

    @Override
    public int createLoanProduct(LoanProductReqDto loanProductReqDto) {

        LoanProduct loanProduct = LoanProduct.builder()
                .name(loanProductReqDto.getName())
                .amountRange(loanProductReqDto.getAmountRange())
                .minRate(loanProductReqDto.getMinRate())
                .preferredRate1(loanProductReqDto.getPreferredRate1())
                .preferredRate2(loanProductReqDto.getPreferredRate2())
                .build();
        loanProductRepository.save(loanProduct);

        return loanProduct.getId();
    }

    @Override
    @Transactional
    public int updateLoanProduct(LoanProductReqDto loanProductReqDto) {

        int id = loanProductReqDto.getId();
        String name = loanProductReqDto.getName();
        BigDecimal amountRange = loanProductReqDto.getAmountRange();
        BigDecimal minRate = loanProductReqDto.getMinRate();
        BigDecimal preferredRate1 = loanProductReqDto.getPreferredRate1();
        BigDecimal preferredRate2 = loanProductReqDto.getPreferredRate2();

        loanProductRepository.updateLoanProductById(id, name, amountRange, minRate, preferredRate1, preferredRate2);

        return id;
    }

    @Override
    public int updateLoanApproval(int id) {

        LoanApproval loanApproval = loanApprovalRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.LOANAPPROVAL_NOT_EXIST));


//        loanApprovalRepository.updateLoanApprovalById(id);

        return id;
    }

    @Override
    public int deleteLoanProduct(int id, LocalDate deletedAt) {

        loanProductRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.LOANPRODUCT_NOT_EXIST));

        loanProductRepository.deleteLoanProductById(id, deletedAt);

        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public LoanProductResDto getLoanProductDetail(int id) {
        LoanProduct loanProduct = loanProductRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.LOANPRODUCT_NOT_EXIST));

        LoanProductResDto loanProductResDto = LoanProductResDto.builder()
                .name(loanProduct.getName())
                .amountRange(loanProduct.getAmountRange().setScale(0, BigDecimal.ROUND_FLOOR))
                .farmerRate24(loanProduct.getMinRate())
                .farmerRate12(loanProduct.getMinRate().add(BigDecimal.valueOf(0.3)))
                .farmerRate06(loanProduct.getMinRate().add(BigDecimal.valueOf(0.6)))
                .trainRate24(loanProduct.getPreferredRate2().subtract(BigDecimal.valueOf(0.6)))
                .trainRate12(loanProduct.getPreferredRate2().subtract(BigDecimal.valueOf(0.3)))
                .trainRate06(loanProduct.getPreferredRate2())
                .build();

        return loanProductResDto;
    }

    @Override
    @Transactional(readOnly = true)
    public MyLoanListResDto getMyLoanList(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
        List<LoanApproval> approvalList = loanApprovalRepository.findAllByUserId(id);
        List<MyLoanListDto> myLoanListResDto = new ArrayList<>();

        for (LoanApproval loanApproval : approvalList) {
            ApprovalStatus approvalStatus = loanApproval.getApproval();
            BigDecimal amount = BigDecimal.valueOf(0);
            int approval = 0; // 반려
            if (approvalStatus == ApprovalStatus.APPROVED) {
                approval = 1; // 승인
            } else if (approvalStatus == ApprovalStatus.PENDING) {
                approval = 2; // 대기
            } else {
                approval = 0;
            }

            MyLoanListDto myLoanListDto = MyLoanListDto.builder()
                    .date(loanApproval.getCreatedAt())
                    .approval(approval)
                    .amount(amount.setScale(0, BigDecimal.ROUND_FLOOR))
                    .id(loanApproval.getId())
                    .name(loanApproval.getLoanOptions().getLoanProduct().getName())
                    .build();

            myLoanListResDto.add(myLoanListDto);

        }

        return MyLoanListResDto.builder()
                .myList(myLoanListResDto)
                .build();
    }


    @Override
    public MyLoanDetailResDto getMyLoanDetail(int id) {

        MyLoanDetailResDto myLoanDetailResDto;

        LoanApproval loanApproval = loanApprovalRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.LOANAPPROVAL_NOT_EXIST));
        int approval;

        if (loanApproval.getApproval() == ApprovalStatus.APPROVED) { // 만약 승인이 완료 되었다면
            LoanAccount loanAccount = loanAccountRepository.findByApprovalId(id);
            approval = 1; // 승인
            myLoanDetailResDto = MyLoanDetailResDto.builder()
                    .approval(approval)
                    .name(loanAccount.getProductName())
                    .startedAt(loanAccount.getStartedAt())
                    .expirationAt(loanAccount.getExpirationAt())
                    .round(loanAccount.getRound())
                    .term(loanAccount.getTerm())
                    .paymentMethod(loanAccount.getPaymentMethod())
                    .paymentDate(loanAccount.getPaymentDate())
                    .principal(loanAccount.getPrincipal())
                    .interest(loanAccount.getInterest())
                    .outstandingAmount(loanAccount.getBalance())
                    .paidPrincipal(loanAccount.getBalance().subtract(loanAccount.getOutstandingPrincipal()).subtract(loanAccount.getOutstandingInterest()))
                    .build();

        } else if (loanApproval.getApproval() == ApprovalStatus.PENDING) { // 만약 대기 중이라면
            approval = 2; // 대기
            myLoanDetailResDto = MyLoanDetailResDto.builder()
                    .approval(approval)
                    .build();

        } else { // 반려되었다면
            approval = 0; // 반려
            myLoanDetailResDto = MyLoanDetailResDto.builder()
                    .approval(approval)
                    .build();
        }


        return myLoanDetailResDto;
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionListResDto getTransactionList(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
        List<TransactionDto> listDto = new ArrayList<>();

        // 대출, 정기예금, 적금, 자유예금 각각 받아오기
        // 받기 전에 통장 유무 확인하기
        List<LoanTransaction> loan = loanTransactionRepository.getMyLoanTransactionList(id);
        List<SavingTransaction> saving = savingTransactionRepository.getMySavingTransactionList(id);
        List<ReDepositTransaction> reDeposit = redepositTransactionRepository.getMyReDepositTransaction(id);
        List<FrDepositTransaction> frDeposit = frDepositTransactionRepository.getMyFrDepositTransactionList(id);


        // 대출
        for (LoanTransaction loanTransaction : loan) {
            String type = "대출";

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNum(loanTransaction.getOppositeAccount())
                    .amount(loanTransaction.getInterest().add(loanTransaction.getPrincipal()))
                    .productName(loanTransaction.getLoanAccount().getProductName())
                    .tansactionDate(loanTransaction.getCreatedAt())
                    .outstanding(loanTransaction.getOutstanding())
                    .transactionType(loanTransaction.getType())
                    .accountType(type)
                    .build();

            listDto.add(transactionDto);
        }

        // 적금
        for (SavingTransaction savingTransaction : saving) {
            String type = "적금";
            
            TransactionDto transactionDto = TransactionDto.builder()
                    .transactionType(savingTransaction.getType())
                    .outstanding(savingTransaction.getRemainingBalance())
                    .productName(savingTransaction.getSavingAccount().getSavingOptions().getSavingProduct().getName())
                    .accountNum(savingTransaction.getOppositeAccount())
                    .amount(savingTransaction.getAmount())
                    .tansactionDate(savingTransaction.getCreatedAt())
                    .accountType(type)
                    .build();

            listDto.add(transactionDto);
        }

        // 정기예금
        for (ReDepositTransaction reDepositTransaction : reDeposit) {
            String type = "정기예금";
            
            TransactionDto transactionDto = TransactionDto.builder()
                    .tansactionDate(reDepositTransaction.getCreatedAt())
                    .amount(reDepositTransaction.getAmount())
                    .productName(reDepositTransaction.getReDepositAccount().getReDepositOptions().getReDepositProduct().getName())
                    .outstanding(reDepositTransaction.getBalance())
                    .transactionType(reDepositTransaction.getType())
                    .accountNum(reDepositTransaction.getReDepositAccount().getAccountNum())
                    .accountType(type)
                    .build();

            listDto.add(transactionDto);
        }

        // 자유예금
        for (FrDepositTransaction frDepositTransaction : frDeposit) {
            String type = "자유예금";
            
            TransactionDto transactionDto = TransactionDto.builder()
                    .transactionType(frDepositTransaction.getType())
                    .amount(frDepositTransaction.getTradeAmount())
                    .accountNum(frDepositTransaction.getOppositeAccount())
                    .outstanding(frDepositTransaction.getRemainingBalance())
                    .productName(frDepositTransaction.getOppositeName())
                    .tansactionDate(frDepositTransaction.getCreatedAt())
                    .accountType(type)
                    .build();

            listDto.add(transactionDto);
        }

        Collections.sort(listDto, (t1, t2) -> t2.getTansactionDate().compareTo(t1.getTansactionDate()));


        return TransactionListResDto.builder()
                .transactionDtos(listDto)
                .build();
    }


}


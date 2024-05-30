package com.ssafy.nhdream.common.utils;

import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.domain.admin.repository.TaskLogRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.AutomaticTransferTaskRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositTransactionRepository;
import com.ssafy.nhdream.domain.loan.repository.LoanAccountRepository;
import com.ssafy.nhdream.domain.loan.repository.LoanTransactionRepository;
import com.ssafy.nhdream.domain.loan.service.LoanBlockchainService;
import com.ssafy.nhdream.domain.redeposit.repository.RedepositTransactionRepository;
import com.ssafy.nhdream.domain.redeposit.service.RedepositBlockchainService;
import com.ssafy.nhdream.domain.saving.repository.SavingAccountRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingTransactionRepository;
import com.ssafy.nhdream.domain.saving.service.SavingBlockchainService;
import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.domain.sse.service.NotificationService;
import com.ssafy.nhdream.entity.admin.JopType;
import com.ssafy.nhdream.entity.admin.TaskLog;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.loan.LoanAccount;
import com.ssafy.nhdream.entity.loan.LoanTransaction;
import com.ssafy.nhdream.entity.redeposit.ReDepositAccount;
import com.ssafy.nhdream.entity.redeposit.ReDepositTransaction;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import com.ssafy.nhdream.entity.saving.SavingTransaction;
import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import com.ssafy.nhdream.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {
    private final NHDCService nhdcService;
    private final FrDepositTransactionRepository frDepositTransactionRepository;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final SavingTransactionRepository savingTransactionRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final RedepositTransactionRepository redepositTransactionRepository;
    private final SavingBlockchainService savingBlockchainService;
    private final RedepositBlockchainService redepositBlockchainService;
    private final AutomaticTransferTaskRepository automaticTransferTaskRepository;
    private final LoanAccountRepository loanAccountRepository;
    private final LoanTransactionRepository loanTransactionRepository;
    private final LoanBlockchainService loanBlockchainService;
    private final TaskLogRepository taskLogRepository;

    // sse
    private final NotificationService notificationService;

    @Transactional
    public void transferFromWalletToWallet(FrDepositAccount senderAccount, FrDepositAccount recipientAccount, BigDecimal amount) {

        //발신자
        User sender = senderAccount.getUser();
        //수신자
        User recipient = recipientAccount.getUser();

        //잔액이 더 적을 때
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new CustomException(ExceptionType.INSUFFICIENT_BALANCE);
        }

        String transactionHash = null;
        //토큰 전송
        try {
            transactionHash = nhdcService.transfer(recipientAccount.getContractAddress(), amount, sender.getWallet().getWalletPrivateKey());
            log.info("발신자Id: {}, 수신자Id: {}", sender.getId(), recipient.getId());
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
            log.error("{} 자유입출금 계좌에서 {} 자유입출금 계좌로 {}금액 이체 실패", senderAccount.getAccountNum(), recipientAccount.getAccountNum(), amount);
            log.error(e.getMessage());

            // 잔액 부족 sse
            notificationService.saveNotice(NotificationType.DREAM_EVENT, sender.getId(), amount, 2, 0, recipient.getName());

            throw new CustomException(ExceptionType.FAILED_TO_TRANSFER_TOKEN);
        }
        //보내는 계좌에서 감소
        senderAccount.transferDecreaseFrDepositBalance(amount);
        //받는 계좌에서 증가
        recipientAccount.transferIncreaseFrDepositBalance(amount);

        //보내는 거래내역 생성
        FrDepositTransaction senderTransaction = FrDepositTransaction.builder()
                .frDepositAccount(senderAccount)
                .type(1)
                .oppositeName(recipient.getName())
                .oppositeAccount(recipientAccount.getContractAddress())
                .tradeAmount(amount)
                .remainingBalance(senderAccount.getBalance())
                .transactionHash(transactionHash)
                .build();

        frDepositTransactionRepository.save(senderTransaction);

        //받는 거래내역 생성
        FrDepositTransaction recipientTransaction = FrDepositTransaction.builder()
                .frDepositAccount(recipientAccount)
                .type(0)
                .oppositeName(sender.getName())
                .oppositeAccount(senderAccount.getContractAddress())
                .tradeAmount(amount)
                .remainingBalance(recipientAccount.getBalance())
                .transactionHash(transactionHash)
                .build();

        frDepositTransactionRepository.save(recipientTransaction);

        // sse 보내기
        notificationService.saveNotice(NotificationType.DREAM_EVENT, recipient.getId(), amount,0, 0, sender.getName());
        notificationService.saveNotice(NotificationType.DREAM_EVENT, sender.getId(), amount,1, 0, recipient.getName());

    }

    @Transactional
    public void transferFromWalletToSaving(FrDepositAccount senderAccount, SavingAccount recipientAccount, BigDecimal amount, LocalDate nextScheduleTime) {

        if (!recipientAccount.getMonthlyAmount().equals(amount)) {
            throw new CustomException(ExceptionType.AMOUNT_NOT_MATCH);
        }

        //잔액이 더 적을 때
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new CustomException(ExceptionType.INSUFFICIENT_BALANCE);
        }

        //발신자
        User sender = senderAccount.getUser();
        //수신자
        User recipient = recipientAccount.getUser();

        String transactionHash = null;
        //토큰 전송
        try {
            transactionHash = savingBlockchainService.depositSavingAccount(recipientAccount.getSavingOptions().getId(), amount, recipient.getWallet().getWalletPrivateKey());

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
            log.error("{} 자유입출금 계좌에서 {} 적금으로 {}금액 이체 실패", senderAccount.getAccountNum(), recipientAccount.getAccountNum(), amount);
            log.error(e.getMessage());
            throw new CustomException(ExceptionType.FAILED_TO_TRANSFER_TOKEN);
        }
        //보내는 계좌에서 감소
        senderAccount.transferDecreaseFrDepositBalance(amount);
        //받는 계좌에서 증가
        recipientAccount.transferFromFrDepositIncreaseBalanceAndUpdateNextDepositDate(amount, nextScheduleTime);

        //보내는 거래내역 생성
        FrDepositTransaction senderTransaction = FrDepositTransaction.builder()
                .frDepositAccount(senderAccount)
                .type(1)
                .oppositeName(recipient.getName())
                .oppositeAccount(recipientAccount.getContractAddress())
                .tradeAmount(amount)
                .remainingBalance(senderAccount.getBalance())
                .transactionHash(transactionHash)
                .build();

        frDepositTransactionRepository.save(senderTransaction);

        //받는 거래내역 생성
        SavingTransaction recipientTransaction = SavingTransaction.builder()
                .savingAccount(recipientAccount)
                .type(0)
                .amount(amount)
                .oppositeAccount(senderAccount.getContractAddress())
                .oppositeName(sender.getName())
                .remainingBalance(recipientAccount.getBalance())
                .transactionHash(transactionHash)
                .build();

        savingTransactionRepository.save(recipientTransaction);
    }

    @Transactional
    public void transferFromWalletToReDeposit(FrDepositAccount senderAccount, ReDepositAccount recipientAccount, BigDecimal amount, String creationHash) {
        log.info("transferFromWalletToReDeposit");

        //잔액이 더 적을 때
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new CustomException(ExceptionType.INSUFFICIENT_BALANCE);
        }

        //발신자
        User sender = senderAccount.getUser();
        //수신자
        User recipient = recipientAccount.getUser();

//        String transactionHash = null;
//
//        try {
//            transactionHash = redepositBlockchainService.depositPrincipalAmount(recipientAccount.getReDepositOptions().getId(), amount, recipient.getWallet().getWalletPrivateKey());
//        } catch (Exception e) {
//            log.error("{} 자유입출금 계좌에서 {} 예금으로 {}금액 이체 실패", senderAccount.getAccountNum(), recipientAccount.getAccountNum(), amount);
//            log.error(e.getMessage());
//            throw new CustomException(ExceptionType.FAILED_TO_TRANSFER_TOKEN);
//        }

        //보내는 계좌에서 감소
        senderAccount.transferDecreaseFrDepositBalance(amount);
        //받는 계좌에서 증가
//        recipientAccount.transferFromFrDepositIncreaseBalance(amount);

        //보내는 거래내역 생성
        FrDepositTransaction senderTransaction = FrDepositTransaction.builder()
                .frDepositAccount(senderAccount)
                .type(1)
                .oppositeName(recipient.getName())
                .oppositeAccount(recipientAccount.getContractAddress())
                .tradeAmount(amount)
                .remainingBalance(senderAccount.getBalance())
                .transactionHash(creationHash)
                .build();

        frDepositTransactionRepository.save(senderTransaction);

        //받는 거래내역 생성
        ReDepositTransaction recipientTransaction = ReDepositTransaction.builder()
                .reDepositAccount(recipientAccount)
                .type(0)
                .amount(amount)
                .oppositeName(sender.getName())
                .balance(recipientAccount.getBalance())
                .transactionHash(creationHash)
                .build();

        redepositTransactionRepository.save(recipientTransaction);
    }

    //대출이체함수
    @Transactional
    public void transferFromWalletToLoan(FrDepositAccount senderAccount, LoanAccount loanAccount, BigDecimal amount, LocalDate nextScheduleTime) {

        // 대출이 완료 되었는지 확인
        if (loanAccount.getState() == 1) {
            throw new CustomException(ExceptionType.LOAN_REPAYMENT_ALREADY);
        }

        // 갚을 이자
        BigDecimal interest = loanAccount.getInterest().divide(BigDecimal.valueOf(loanAccount.getTerm()),0, RoundingMode.DOWN);

        // 갚을 원금
        BigDecimal principal = loanAccount.getPrincipal().divide(BigDecimal.valueOf(loanAccount.getTerm()),0,RoundingMode.DOWN);

        // 갚을 돈 (이번달에 나갈 돈)
        BigDecimal monthlyAmount = interest.add(principal);

        //내야하는 돈과 양이 다를 때
        if (!monthlyAmount.equals(amount.setScale(0,RoundingMode.DOWN))) {
            log.info("monthlyAmount :{}, amount : {}", monthlyAmount, amount);
            throw new CustomException(ExceptionType.AMOUNT_NOT_MATCH);
        }

        //잔액이 더 적을 때
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new CustomException(ExceptionType.INSUFFICIENT_BALANCE);
        }

        // 블록체인 거래 후 loanAccount 수정 아직 블록체인 거래 안넣음
        String transactionHash = null;

        try {
            transactionHash = loanBlockchainService.makeMonthlyPayment(loanAccount.getLoanOptions().getLoanProduct().getId(),loanAccount.getUser().getWallet().getWalletPrivateKey());
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
            log.error("{} 자유입출금 계좌에서 {} 대출로 {}금액 이체 실패", senderAccount.getAccountNum(), loanAccount.getAccountNum(), amount);
            log.error(e.getMessage());
            throw new CustomException(ExceptionType.FAILED_TO_TRANSFER_TOKEN);
        }

        // 수정된 원금 잔액, 이자 잔액, 원리금 잔액, 납입 회차+1
        BigDecimal newInterest = loanAccount.getOutstandingInterest().subtract(interest);
        BigDecimal newPrincipal = loanAccount.getOutstandingPrincipal().subtract(principal);
        BigDecimal newAmount = loanAccount.getBalance().subtract(amount);

        loanAccountRepository.updateBalance(loanAccount.getId(), newInterest, newPrincipal, newAmount);

        // loanAccount 수정 후 LoanTransaction 생성
        LoanTransaction loanTransaction = LoanTransaction.builder()
                .type(0)
                .interest(interest)
                .principal(principal)
                .oppositeAccount(senderAccount.getAccountNum())
                .oppositeName(senderAccount.getContractAddress())
                .outstanding(newAmount)
                .loanAccount(loanAccount)
                .build();

        loanTransactionRepository.save(loanTransaction);

        //돈 가져온 자유입출금계좌 내역 수정

        senderAccount.transferDecreaseFrDepositBalance(newAmount);

        FrDepositTransaction senderTransaction = FrDepositTransaction.builder()
                .frDepositAccount(senderAccount)
                .type(1)
                .oppositeName(loanAccount.getProductName())
                .oppositeAccount(loanAccount.getAccountNum())
                .tradeAmount(newAmount)
                .remainingBalance(senderAccount.getBalance())
                .transactionHash(transactionHash)
                .build();

        frDepositTransactionRepository.save(senderTransaction);


        // 납입회차와 대출기간이 같아졌는지 확인
        // 같다면 남은 잔액이 0원인지 확인
        if (loanAccount.getRound().equals(loanAccount.getTerm())) {
            if (loanAccount.getBalance().compareTo(BigDecimal.valueOf(0)) == 0) {
                // 대출 상환 완료 (대출 통장 상태 1로 변경)
                loanAccountRepository.finishLoan(loanAccount.getId());
            }

        }

    }


// 이제 배치로전환됨
//    //자동이체함수 (자유입출금)
//    @Transactional
//    @Scheduled(cron = "0 20 22 * * *")
//    public void processAutoTransferFromWalletToWallet() {
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//
//        List<AutomaticTransferTask> automaticTransferTaskFrDepositList = automaticTransferTaskRepository.getAutomaticTransferTaskFrDeposit(yesterday, today);
//
//        for (AutomaticTransferTask automaticTransferToFrDepositTask : automaticTransferTaskFrDepositList) {
//            //보내는사람계좌
//            try {
//                FrDepositAccount senderAccount = automaticTransferToFrDepositTask.getSenderWalletAccount();
//                //받는사람계좌
//                FrDepositAccount recipientAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(automaticTransferToFrDepositTask.getRecipientAccount())
//                        .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));
//                //계좌이체 진행
//                transferFromWalletToWallet(senderAccount, recipientAccount, automaticTransferToFrDepositTask.getRecurringAmount());
//
//                //계좌이체 했으면 계좌이체 항목 업데이트
//                LocalDate nextScheduleTime = CalculateDate.nextValidDate(today.plusMonths(1), automaticTransferToFrDepositTask.getTransferDay());
//
//                automaticTransferToFrDepositTask.updateNextScheduleTime(nextScheduleTime);
//
//                //자동이체 다 끝났으면
//                if (nextScheduleTime.isAfter(automaticTransferToFrDepositTask.getExpiredAt())) {
//                    automaticTransferToFrDepositTask.updatedIsActive();
//                }
//
//
//                log.info("자동이체Id: {},userId: {} 성공",automaticTransferToFrDepositTask.getId(),automaticTransferToFrDepositTask.getUser().getId());
//            } catch (CustomException e) {
//                TaskLog taskLog = TaskLog.builder()
//                        .isSuccess(false)
//                        .isProcessed(false)
//                        .jobType(JopType.AutoTransfer)
//                        .errorType(e.getExceptionType().toString())
//                        .errorMessage(e.getMessage())
//                        .errorDetail(e.toString())
//                        .build();
//                taskLogRepository.save(taskLog);
//                log.error("자동이체Id: {} 실패 에러메시지 : {}", automaticTransferToFrDepositTask.getId(), e.getMessage());
//            } catch (Exception e) {
//                TaskLog taskLog = TaskLog.builder()
//                        .isSuccess(false)
//                        .isProcessed(false)
//                        .jobType(JopType.AutoTransfer)
//                        .errorType(e.getClass().toString())
//                        .errorMessage(e.getMessage())
//                        .errorDetail(e.toString())
//                        .build();
//                taskLogRepository.save(taskLog);
//                // 예상치 못한 다른 예외들 처리
//                log.error("예외처리한 예외말고 다른 예외 발생", e);
//            }
//        }
//    }
//
//    //자동이체 지갑에서 적금으로
//    @Transactional
//    @Scheduled(cron = "0 30 23 * * *")
//    public void processAutoTransferFromWalletToSaving() {
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//
//        List<AutomaticTransferTask> automaticTransferToSavingTaskList = automaticTransferTaskRepository.getAutomaticTransferTaskSaving(yesterday, today);
//
//
//        for (AutomaticTransferTask automaticTransferToSavingTask : automaticTransferToSavingTaskList) {
//            try {
//                LocalDate nextScheduleTime = CalculateDate.nextValidDate(today.plusMonths(1), automaticTransferToSavingTask.getTransferDay());
//
//                //보내는 지갑 찾기
//                FrDepositAccount senderAccount = automaticTransferToSavingTask.getSenderWalletAccount();
//
//                //돈 넣을 적금계좌 찾기
//                SavingAccount recipientAccount = savingAccountRepository.findByAccountNum(automaticTransferToSavingTask.getRecipientAccount())
//                        .orElseThrow(() -> new CustomException(ExceptionType.SAVINGACCOUNT_NOT_EXIST));
//
//                //계좌이체 진행
//                transferFromWalletToSaving(senderAccount, recipientAccount, automaticTransferToSavingTask.getRecurringAmount(), nextScheduleTime);
//
//
//                //계좌이체 했으니 항목업데이트
//                automaticTransferToSavingTask.updateNextScheduleTime(nextScheduleTime);
//
//                //만기됐으면 자동이체 끝
//                if (!recipientAccount.isActive()) {
//                    automaticTransferToSavingTask.updatedIsActive();
//                }
//
//                log.info("userId: {}, 자동이체Id: {} 성공", automaticTransferToSavingTask.getUser().getId(), automaticTransferToSavingTask.getId());
//
//
//            } catch (CustomException e) {
//                TaskLog taskLog = TaskLog.builder()
//                        .isSuccess(false)
//                        .isProcessed(false)
//                        .jobType(JopType.AutoTransfer)
//                        .errorType(e.getExceptionType().toString())
//                        .errorMessage(e.getMessage())
//                        .errorDetail(e.toString())
//                        .build();
//                taskLogRepository.save(taskLog);
//                log.error("자동이체Id: {} 실패 에러메시지 : {}", automaticTransferToSavingTask.getId(), e.getMessage(), e);
//            } catch (Exception e) {
//                // 예상치 못한 다른 예외들 처리
//                TaskLog taskLog = TaskLog.builder()
//                        .isSuccess(false)
//                        .isProcessed(false)
//                        .jobType(JopType.AutoTransfer)
//                        .errorType(e.getClass().toString())
//                        .errorMessage(e.getMessage())
//                        .errorDetail(e.toString())
//                        .build();
//                taskLogRepository.save(taskLog);
//                log.error("예외처리한 예외말고 다른 예외 발생", e);
//            }
//        }
//    }
//
//    //자동이체 지갑에서 대출로
//    @Transactional
//    @Scheduled(cron = "0 30 23 * * *")
//    public void processAutoTransferFromWalletToLoan() {
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//
//        List<AutomaticTransferTask> automaticTransferToLoanTaskList = automaticTransferTaskRepository.getAutomaticTransferTaskLoan(yesterday, today);
//
//
//        for (AutomaticTransferTask automaticTransferToLoanTask : automaticTransferToLoanTaskList) {
//            try {
//                LocalDate nextScheduleTime = CalculateDate.nextValidDate(today.plusMonths(1), automaticTransferToLoanTask.getTransferDay());
//
//                //보내는 지갑 찾기
//                FrDepositAccount senderAccount = automaticTransferToLoanTask.getSenderWalletAccount();
//
//                //돈 넣을 대출계좌 찾기
//                LoanAccount loanAccount = loanAccountRepository.findByAccountNum(automaticTransferToLoanTask.getRecipientAccount())
//                        .orElseThrow(() -> new CustomException(ExceptionType.SAVINGACCOUNT_NOT_EXIST));
//
//                //계좌이체 진행 위에 함수 정의되어있음
//                transferFromWalletToLoan(senderAccount, loanAccount, automaticTransferToLoanTask.getRecurringAmount(), nextScheduleTime);
//
//
//                //계좌이체 했으니 항목업데이트
//                automaticTransferToLoanTask.updateNextScheduleTime(nextScheduleTime);
//
//                //만기됐으면 자동이체 끝
//                if (loanAccount.getState() == 1) {
//                    automaticTransferToLoanTask.updatedIsActive();
//                }
//
//                log.info("userId: {}, 자동이체Id: {} 성공", automaticTransferToLoanTask.getUser().getId(), automaticTransferToLoanTask.getId());
//
//
//            }
//            catch (CustomException e) {
//                TaskLog taskLog = TaskLog.builder()
//                        .isSuccess(false)
//                        .isProcessed(false)
//                        .jobType(JopType.AutoTransfer)
//                        .errorType(e.getExceptionType().toString())
//                        .errorMessage(e.getMessage())
//                        .errorDetail(e.toString())
//                        .build();
//                taskLogRepository.save(taskLog);
//                log.error("자동이체Id: {} 실패 에러메시지 : {}", automaticTransferToLoanTask.getId(), e.getMessage(), e);
//            } catch (Exception e) {
//                TaskLog taskLog = TaskLog.builder()
//                        .isSuccess(false)
//                        .isProcessed(false)
//                        .jobType(JopType.AutoTransfer)
//                        .errorType(e.getClass().toString())
//                        .errorMessage(e.getMessage())
//                        .errorDetail(e.toString())
//                        .build();
//                taskLogRepository.save(taskLog);
//                // 예상치 못한 다른 예외들 처리
//                log.error("예외처리한 예외말고 다른 예외 발생", e);
//            }
//        }
//    }

}

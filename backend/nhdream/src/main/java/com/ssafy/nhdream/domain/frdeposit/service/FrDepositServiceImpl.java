package com.ssafy.nhdream.domain.frdeposit.service;

import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.common.utils.CalculateDate;
import com.ssafy.nhdream.common.utils.TransferService;
import com.ssafy.nhdream.domain.frdeposit.dto.*;
import com.ssafy.nhdream.domain.frdeposit.repository.AutomaticTransferTaskRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositTransactionRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.RecentTransferAddressRepository;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import com.ssafy.nhdream.entity.transfer.RecentTransferAddress;
import com.ssafy.nhdream.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FrDepositServiceImpl implements FrDepositService {
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final FrDepositTransactionRepository frDepositTransactionRepository;
    private final UserRepository userRepository;
    private final NHDCService nhdcService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AutomaticTransferTaskRepository automaticTransferTaskRepository;
    private final TransferService transferService;
    private final RecentTransferAddressRepository recentTransferAddressRepository;


    @Value("${spring.NHDC.privateKey}")
    private String serverPrivateKey;

    //지갑주소로 계좌주명 조회
    public CheckFrDepositAccountNameResDto checkOppositeName(String walletAddress) {
        //지갑주소의 자유입출금계좌가 있는지 조회
        FrDepositAccount frDepositAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(walletAddress)
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

        return CheckFrDepositAccountNameResDto.builder().oppositeName(frDepositAccount.getUser().getName())
                .oppositeAccountNum(frDepositAccount.getAccountNum())
                .oppositeContractAddress(frDepositAccount.getContractAddress())
                .build();
    }

    //지갑주소 + 시작일이 있으면 계좌주+ 시작일자 조회
    public CheckFrDepositAccountNameResDto checkOppositeNameAndStartDate(String walletAddress, int transferDate) {
        //지갑주소의 자유입출금계좌가 있는지 조회
        FrDepositAccount frDepositAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(walletAddress)
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));
        LocalDate startDate = CalculateDate.findNextPaymentDate(transferDate);

        return CheckFrDepositAccountNameResDto.builder().oppositeName(frDepositAccount.getUser().getName())
                .oppositeAccountNum(frDepositAccount.getAccountNum())
                .oppositeContractAddress(frDepositAccount.getContractAddress())
                .startDate(startDate)
                .build();
    }

    //자유입출금 계좌 거래내역 조회
    public List<FrDepositTransactionListResDto> getFrDepositTransactionList(String frDepositAccountAddress, int userId, int page, int size) {
        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
        //해당계약주소의 자유입출금계좌가 없을 때
        FrDepositAccount frDepositAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(frDepositAccountAddress)
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));
        //계좌 주인과 로그인 유저가 다를 때
        if (!frDepositAccount.getUser().equals(loginUser)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED_FRDEPOSITACCOUNT_ACCESS);
        }
        Page<FrDepositTransaction> frDepositTransactions = frDepositTransactionRepository.getFrDepositTransactionList(frDepositAccount, PageRequest.of(page, size));
        return frDepositTransactions.getContent().stream().map(frDepositTransaction -> FrDepositTransactionListResDto.builder()
                .id(frDepositTransaction.getId())
                .type(frDepositTransaction.getType())
                .oppositeAccount(frDepositTransaction.getOppositeAccount())
                .oppositeName(frDepositTransaction.getOppositeName())
                .remainingBalance(frDepositTransaction.getRemainingBalance())
                .transactionHash(frDepositTransaction.getTransactionHash())
                .tradeAmount(frDepositTransaction.getTradeAmount().setScale(0, RoundingMode.DOWN).longValueExact())
                .tradedAt(frDepositTransaction.getCreatedAt())
                .build())
                .collect(Collectors.toList());

    }

    //자기지갑 잔액조회
    public FrDepositBalanceResDto getFrDepositBalance(int userId) {
        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
        //해당유저의 자유입출금계좌가 없을 때
        Optional<FrDepositAccount> findAccount = frDepositAccountRepository.findByUser(loginUser);

        if (findAccount.isEmpty()) {
            throw new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST);
        }

        FrDepositAccount userFrAccount = findAccount.get();
        //계좌 주인과 로그인 유저가 다를 때
        if (!userFrAccount.getUser().equals(loginUser)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED_FRDEPOSITACCOUNT_ACCESS);
        }

        return FrDepositBalanceResDto.builder()
                .balance(userFrAccount.getBalance()).build();
    }


    //계좌이체
    @Transactional
    public BigDecimal transferFrDeposit(FrDepositTransferReqDto frDepositTransferReqDto, int userId) {
        //거래량
        BigDecimal tradeAmount = frDepositTransferReqDto.getAmount();


        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        //비번이 틀릴 때
        if (!bCryptPasswordEncoder.matches(frDepositTransferReqDto.getPassword(), loginUser.getWalletPassword())) {
            throw new CustomException(ExceptionType.WALLETPASSWORD_NOT_MATCH);

        }

        //해당유저의 자유입출금계좌가 없을 때
        FrDepositAccount senderFrAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(loginUser.getWallet().getWalletAddress())
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));
        //보내는 주소의 계좌가 없을 때
        FrDepositAccount recipientAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(frDepositTransferReqDto.getRecipientWalletAddress())
                .orElseThrow(() -> new CustomException(ExceptionType.RECIPIENTACCOUNT_NOT_EXIST));

        //같은 계좌에서 같은 계좌로 보낼 때
        if (frDepositTransferReqDto.getRecipientWalletAddress().equals(senderFrAccount.getContractAddress())) {
            throw new CustomException(ExceptionType.SELF_TRANSFER_NOT_ALLOWED);
        }

        //받는 사람의 유저
        User recipientUser = recipientAccount.getUser();


        //계좌 주인과 로그인 유저가 다를 때
        if (!senderFrAccount.getUser().equals(loginUser)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED_FRDEPOSITACCOUNT_ACCESS);
        }

        //잔액이 부족할 때
        if (senderFrAccount.getBalance().compareTo(tradeAmount)<0) {
            throw new CustomException(ExceptionType.INSUFFICIENT_BALANCE);
        }

        transferService.transferFromWalletToWallet(senderFrAccount,recipientAccount,tradeAmount);

        //최근 이체내역 조회후 기존에 있는 내역이면 최근썼다고 신규 내역이면 업데이트 5개 이상이면 맨마지막꺼 지우고 저장
        Optional<RecentTransferAddress> existingAddress  = recentTransferAddressRepository.findByUserAndRecipientAddress(loginUser, recipientAccount.getContractAddress());
        if (existingAddress.isPresent()) {
            RecentTransferAddress recentTransferAddress = existingAddress.get();
            recentTransferAddress.updateRecentTransferAddressUpdatedAt();
        } else {
            List<RecentTransferAddress> recentTransferAddressList = recentTransferAddressRepository.findTop5ByUserIdOrderByUpdatedAtDesc(loginUser.getId());
            if (recentTransferAddressList.size() >= 5) {
                RecentTransferAddress oldestAddress = recentTransferAddressList.getLast();
                recentTransferAddressRepository.delete(oldestAddress);
            }

            RecentTransferAddress newRecentTransferAddress = RecentTransferAddress.builder()
                    .user(loginUser)
                    .recipientAddress(recipientAccount.getContractAddress())
                    .recipientName(recipientUser.getName())
                    .build();
            recentTransferAddressRepository.save(newRecentTransferAddress);

        }
        return senderFrAccount.getBalance();
    }

    //자동이체생성
    @Transactional
    public void createAutomaticTransfer(CreateAutomaticTransferReqDto createAutomaticTransferReqDto, int userId) {

        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        //지갑비번 확인
        if (!bCryptPasswordEncoder.matches(createAutomaticTransferReqDto.getWalletPassword(), loginUser.getWalletPassword())) {
            throw new CustomException(ExceptionType.WALLETPASSWORD_NOT_MATCH);
        }
        //출금계좌찾기
        FrDepositAccount senderAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(createAutomaticTransferReqDto.getSenderWalletAddress())
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

        //출금계좌주와 로그인한 유저가 같은지 확인
        if (!senderAccount.getUser().equals(loginUser)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED_FRDEPOSITACCOUNT_ACCESS);
        }

        //상대계좌찾기
        FrDepositAccount recipientAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(createAutomaticTransferReqDto.getRecipientWalletAddress())
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));


        //이체날을 기준으로 시작일, 끝나는 날계산
        LocalDate startDate = CalculateDate.findNextPaymentDate(createAutomaticTransferReqDto.getTransferDay());
        LocalDate endDate = startDate.plusMonths(createAutomaticTransferReqDto.getTerm());
        //다음 이체 예정일을 시작일로 지정
        //객체 생성
        AutomaticTransferTask automaticTransferTask = AutomaticTransferTask.builder()
                .user(loginUser)
                .senderWalletAccount(senderAccount)
                .recipientAccount(recipientAccount.getContractAddress())
                .recipientName(recipientAccount.getUser().getName())
                .recurringAmount(createAutomaticTransferReqDto.getRecurringAmount())
                .transferDay(createAutomaticTransferReqDto.getTransferDay())
                .nextScheduleTime(startDate)
                .startedAt(startDate)
                .expiredAt(endDate)
                .term(createAutomaticTransferReqDto.getTerm())
                .build();

        automaticTransferTaskRepository.save(automaticTransferTask);
    }

    //자동이체내역조회
    public List<GetAutomaticTransferLogResDto> getAutomaticTransferList(int userId, int page, int size) {
        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        Page<AutomaticTransferTask> automaticTransferTasks = automaticTransferTaskRepository.getAutomaticTransferTaskList(loginUser, PageRequest.of(page, size));
        return automaticTransferTasks.getContent().stream().map(automaticTransferTask -> GetAutomaticTransferLogResDto.builder()
                        .id(automaticTransferTask.getId())
                        .recipientWalletAddress(automaticTransferTask.getRecipientAccount())
                        .recipientName(automaticTransferTask.getRecipientName())
                        .amount(automaticTransferTask.getRecurringAmount().setScale(0,RoundingMode.DOWN).longValueExact())
                        .startedAt(automaticTransferTask.getStartedAt())
                        .expiredAt(automaticTransferTask.getExpiredAt())
                        .transferDay(automaticTransferTask.getTransferDay())
                        .nextScheduledTransferDate(automaticTransferTask.getNextScheduleTime())
                        .type(automaticTransferTask.getType())
                        .build())
                .collect(Collectors.toList());

    }


    //자동이체상세조회
    @Override
    public GetAutomaticTransferDetailResDto getAutomaticTransferDetail(int autoTransferId, int userId) {
        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        //자동이체 찾기
        AutomaticTransferTask automaticTransferTask = automaticTransferTaskRepository.findById(autoTransferId)
                .orElseThrow(() -> new CustomException(ExceptionType.AUTOTRANSFER_NOT_EXIST));

        //로그인한 유저와 자동이체 주인이 같은지 보기
        if (!automaticTransferTask.getUser().equals(loginUser)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED_AUTOTRANSFERINFO_ACCESS);
        }

        return GetAutomaticTransferDetailResDto.builder()
                .id(automaticTransferTask.getId())
                .amount(automaticTransferTask.getRecurringAmount())
                .transferDay(automaticTransferTask.getTransferDay())
                .expiredDate(CalculateDate.changeDateTimeFormat(automaticTransferTask.getExpiredAt()))
                .build();





    }



    //자동이체삭제
    @Transactional
    public void deleteAutomaticTransfer(int autoTransferId, int userId) {
        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        //해당Id의 자동이체가 없을 때
        AutomaticTransferTask automaticTransferTask = automaticTransferTaskRepository.findById(autoTransferId
        ).orElseThrow(()-> new CustomException(ExceptionType.AUTOTRANSFER_NOT_EXIST));

        automaticTransferTaskRepository.delete(automaticTransferTask);
    }

    //자동이체변경
    @Transactional
    @Override
    public void updateAutomaticTransfer(PatchAutoTransferReqDto patchAutoTransferReqDto, int autoTransferId, int userId) {
        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        //해당Id의 자동이체가 없을 때
        AutomaticTransferTask automaticTransferTask = automaticTransferTaskRepository.findById(autoTransferId
        ).orElseThrow(()-> new CustomException(ExceptionType.AUTOTRANSFER_NOT_EXIST));

        int transferDate = patchAutoTransferReqDto.getTransferDate();
        LocalDate expiredAt = patchAutoTransferReqDto.getExpiredAt();
        int term = (int) ChronoUnit.MONTHS.between(automaticTransferTask.getStartedAt(), expiredAt);
        LocalDate nextScheduleTime = CalculateDate.findNextPaymentDate(transferDate);
        if (automaticTransferTask.getStartedAt().isAfter(nextScheduleTime)) {
            nextScheduleTime = CalculateDate.nextValidDate(automaticTransferTask.getStartedAt().plusMonths(1), transferDate);
        }

        //자동이체기록변환
        automaticTransferTask.updateTermAndDateAndAmount(patchAutoTransferReqDto.getAmount(),patchAutoTransferReqDto.getTransferDate(),patchAutoTransferReqDto.getExpiredAt(),term,nextScheduleTime);

    }

    //최근 이체한 내역 조회
    @Override
    public List<RecentTransferAddressListResDto> getRecentTransferAddressList(int userId) {
        //로그인한 유저가 없을 때(이상한 유저일 때)
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
        //업데이트시간을 기준으로 5개 조회
        List<RecentTransferAddress> recentTransferAddressList = recentTransferAddressRepository.findTop5ByUserIdOrderByUpdatedAtDesc(userId);
        //dto로 변환후 반환
        return recentTransferAddressList.stream().map(recentTransferAddress -> RecentTransferAddressListResDto.builder()
                .id(recentTransferAddress.getId())
                .name(recentTransferAddress.getRecipientName())
                .address(recentTransferAddress.getRecipientAddress())
                .build()).collect(Collectors.toList());
    }
}

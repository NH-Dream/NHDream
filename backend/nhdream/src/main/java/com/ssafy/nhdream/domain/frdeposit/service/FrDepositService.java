package com.ssafy.nhdream.domain.frdeposit.service;

import com.ssafy.nhdream.domain.frdeposit.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface FrDepositService {

    //지갑주소로 계좌주명 조회
    CheckFrDepositAccountNameResDto checkOppositeName(String walletAddress);

    //지갑주소 + 지정일자로 계좌주명 + 첫 시작일 조회
    CheckFrDepositAccountNameResDto checkOppositeNameAndStartDate(String walletAddress, int transferDate);


        //자유입출금 계좌 거래내역 조회
    List<FrDepositTransactionListResDto> getFrDepositTransactionList(String frDepositAccountAddress, int userId, int page, int size);

    //자기지갑 잔액 조회
    FrDepositBalanceResDto getFrDepositBalance(int userId);

    //계좌이체
    BigDecimal transferFrDeposit(FrDepositTransferReqDto frDepositTransferReqDto, int userId);

    //자동이체
    void createAutomaticTransfer(CreateAutomaticTransferReqDto createAutomaticTransferReqDto, int userId);

    //자동이체상세조회
    GetAutomaticTransferDetailResDto getAutomaticTransferDetail(int autoTransferId, int userId);

    //자동이체내역조회
    List<GetAutomaticTransferLogResDto> getAutomaticTransferList(int userId, int page, int size);

    //자동이체삭제
    void deleteAutomaticTransfer(int autoTransferId, int userId);

    //자동이체변경
    void updateAutomaticTransfer(PatchAutoTransferReqDto patchAutoTransferReqDto,int autoTransferId, int userId);

    //최근 이체한 거래내역 조회
    List<RecentTransferAddressListResDto> getRecentTransferAddressList(int userId);

}

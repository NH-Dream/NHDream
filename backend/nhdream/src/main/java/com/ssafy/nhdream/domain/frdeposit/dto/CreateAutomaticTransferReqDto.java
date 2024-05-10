package com.ssafy.nhdream.domain.frdeposit.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateAutomaticTransferReqDto {

    //보낼주소
    private String recipientWalletAddress;

    //이체금액
    private BigDecimal recurringAmount;

    //입금주소
    private String senderWalletAddress;

    //이체기간
    private int term;

    //매달지정일
    private int transferDay;

    //지갑비번
    private String walletPassword;
}

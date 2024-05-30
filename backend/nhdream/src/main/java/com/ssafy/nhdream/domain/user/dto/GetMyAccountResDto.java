package com.ssafy.nhdream.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetMyAccountResDto {

    //타입(0:예금 1:적금)
    private int type;

    //계좌id
    private int accountId;

    //상품옵션id
    private int optionId;

    //계좌상품이름
    private String name;

    //잔액
    private BigDecimal balance;

    //계좌번호
    private String accountNum;

    //블록체인 주소
    private String contractAddress;

    //생성일
    private LocalDateTime createdAt;
}

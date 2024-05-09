package com.ssafy.nhdream.domain.saving.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SavingHistoryResDto {
    //상품이름
    private String name;

    //계좌번호
    private String accountNum;

    //잔액
    private long balance;

    //거래내역
    private List<SavingTransactionResDto> savingTransactionList;

}

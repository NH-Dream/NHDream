package com.ssafy.nhdream.domain.redeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "예금 계좌 거래 내역 응답 DTO")
public class AccountHistoryResDto {

    @Schema(description = "상품이름")
    private String name;

    @Schema(description = "계좌번호")
    private String accountNum;

    @Schema(description = "현재잔액")
    private long balance;

    @Schema(description = "거래 내역리스트")
    private List<ReDepositTransactionDto> reDepositTransactionList;
}


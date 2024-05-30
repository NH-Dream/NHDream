package com.ssafy.nhdream.domain.redeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "예금 계좌 거래 내역 DTO")
public class ReDepositTransactionDto {

    @Schema(description = "거래일자")
    private LocalDate transactionDate;

    @Schema(description = "거래금액")
    private long transactionAmount;

    @Schema(description = "잔액")
    private long transactionBalance;

    @Schema(description = "거래 타입")
    private int type;

    @Schema(description = "거래자명")
    private String traderName;
}

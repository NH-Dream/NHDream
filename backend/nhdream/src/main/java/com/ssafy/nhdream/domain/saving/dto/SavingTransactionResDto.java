package com.ssafy.nhdream.domain.saving.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SavingTransactionResDto {
    private int id;
    private int type;
    private String oppositeAccount;
    private String oppositeName;
    private BigDecimal remainingBalance;
    private String transactionHash;

    private BigDecimal tradeAmount;
    private LocalDate tradedAt;
    //이체금액, 거래시간

    @Builder
    public SavingTransactionResDto(int id, int type, String oppositeAccount, String oppositeName,
                                   BigDecimal remainingBalance, String transactionHash,
                                   BigDecimal tradeAmount, LocalDate tradedAt) {
        this.id = id;
        this.type = type;
        this.oppositeAccount = oppositeAccount;
        this.oppositeName = oppositeName;
        this.remainingBalance = remainingBalance;
        this.transactionHash = transactionHash;
        this.tradeAmount = tradeAmount;
        this.tradedAt = tradedAt;
    }
}

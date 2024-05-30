package com.ssafy.nhdream.domain.frdeposit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FrDepositTransactionListResDto {

        private int id;
        private int type;
        private String oppositeAccount;
        private String oppositeName;
        private BigDecimal remainingBalance;
        private String transactionHash;
        private long tradeAmount;
        private LocalDateTime tradedAt;

        @Builder
        public FrDepositTransactionListResDto(int id, int type, String oppositeAccount, String oppositeName,
                                           BigDecimal remainingBalance, String transactionHash,
                                              long tradeAmount, LocalDateTime tradedAt) {
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

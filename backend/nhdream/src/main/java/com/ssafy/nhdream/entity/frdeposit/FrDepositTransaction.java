package com.ssafy.nhdream.entity.frdeposit;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "FR_DEPOSIT_TRANSACTION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FrDepositTransaction extends BaseTimeEntity {

    //주식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //자유입출금통장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fr_deposit_id", nullable = false)
    private FrDepositAccount frDepositAccount;

    //거래종류(0:이체들어온것 , 1:이체나간것)
    @Column(name = "fr_deposit_type", nullable = false)
    private int type;

    @Column(name = "fr_deposit_opposite_account_num", nullable = false)
    private String oppositeAccount;

    @Column(name = "fr_deposit_opposite_name", nullable = false)
    private String oppositeName;

    @Column(name = "fr_deposit_trade_amount", nullable = false)
    private BigDecimal tradeAmount;

    @Column(name = "fr_deposit_remaining_balance", nullable = false)
    private BigDecimal remainingBalance;

    @Column(name = "fe_deposit_trade_hash", nullable = false)
    private String transactionHash;

    @Builder
    public FrDepositTransaction(FrDepositAccount frDepositAccount, int type, String oppositeAccount, String oppositeName,
                                BigDecimal tradeAmount, BigDecimal remainingBalance, String transactionHash) {
        this.frDepositAccount = frDepositAccount;
        this.type = type;
        this.oppositeAccount = oppositeAccount;
        this.oppositeName = oppositeName;
        this.tradeAmount = tradeAmount;
        this.remainingBalance = remainingBalance;
        this.transactionHash = transactionHash;

    }
}

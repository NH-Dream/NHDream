package com.ssafy.nhdream.entity.redeposit;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "re_deposit_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReDepositTransaction extends BaseTimeEntity {

    // 식별 ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // FK (정기예금 통장 PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "re_deposit_account_id")
    private ReDepositAccount reDepositAccount;

    // 거래 종류 (입금, 출금)
    @Column(name = "re_deposit_type", nullable = false)
    private int type;

    // 거래 금액
    @Column(name = "re_deposit_amount", nullable = false)
    private BigDecimal amount;

//    // 상대 계좌번호
//    @Column(name = "re_deposit_trade_opposite_account", nullable = false)
//    private String oppositeAccount;
//
    // 상대 계좌주명
    @Column(name = "re_deposit_trade_opposite_name", nullable = false)
    private String oppositeName;

    // 잔액
    @Column(name = "re_deposit_balance", nullable = false)
    private BigDecimal balance;

    //트랜잭션해쉬
    @Column(name = "re_deposit_trade_transacionhash")
    private String transactionHash;

    // 생성자
    @Builder

    public ReDepositTransaction(ReDepositAccount reDepositAccount, int type, BigDecimal amount, String oppositeName, BigDecimal balance, String transactionHash) {
        this.reDepositAccount = reDepositAccount;
        this.type = type;
        this.amount = amount;
        this.oppositeName = oppositeName;
        this.balance = balance;
        this.transactionHash = transactionHash;
    }

    //    public ReDepositTransaction(int type, BigDecimal amount, String oppositeAccount, String oppositeName, BigDecimal balance, ReDepositAccount reDepositAccount) {
//        this.type = type;
//        this.amount = amount;
//        this.oppositeAccount = oppositeAccount;
//        this.oppositeName = oppositeName;
//        this.balance = balance;
//        this.reDepositAccount = reDepositAccount;
//    }



}

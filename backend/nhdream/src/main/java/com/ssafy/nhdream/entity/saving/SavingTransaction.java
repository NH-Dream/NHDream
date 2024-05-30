package com.ssafy.nhdream.entity.saving;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "saving_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingTransaction extends BaseTimeEntity {

    //식별 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //외래키영역
    //거래계좌매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_account_id")
    private SavingAccount savingAccount;

    //거래 타입(0이 돈들어오는 이체 1이 돈 나가는 이체)
    @Column(name = "saving_trade_type",nullable = false)
    private int type;

    //거래량
    @Column(name = "saving_trade_amount", nullable = false)
    private BigDecimal amount;

    //거래상대계좌번호
    @Column(name = "saving_trade_opposite_account", nullable = false)
    private String oppositeAccount;

    //거래상대계좌주명
    @Column(name = "saving_trade_opposite_name", nullable = false)
    private String oppositeName;

    //거래후잔액
    @Column(name = "saving_remaining_balance", nullable = false)
    private BigDecimal remainingBalance;

    //트랜잭션해쉬
    @Column(name = "saving_trade_transacionhash")
    private String transactionHash;

    //생성자
    @Builder
    public SavingTransaction(int type, BigDecimal amount, String oppositeAccount, String oppositeName,
                             BigDecimal remainingBalance, SavingAccount savingAccount,String transactionHash) {
        this.type = type;
        this.amount = amount;
        this.oppositeAccount = oppositeAccount;
        this.oppositeName = oppositeName;
        this.remainingBalance = remainingBalance;
        this.savingAccount = savingAccount;
        this.transactionHash = transactionHash;
    }
}

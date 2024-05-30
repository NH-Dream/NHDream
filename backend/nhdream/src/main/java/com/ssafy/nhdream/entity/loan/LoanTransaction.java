package com.ssafy.nhdream.entity.loan;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "loan_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanTransaction extends BaseTimeEntity {

    // PK
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 거래 타입
    @Column(name = "loan_type", nullable = false)
    private int type;

    // 갚은 대출 원금
    @Column(name = "loan_principal", nullable = false)
    private BigDecimal principal;

    // 갚은 대출 이자
    @Column(name = "loan_interest", nullable = false)
    private BigDecimal interest;

    // 거래 상대 계좌 번호
    @Column(name = "loan_opposite_account", nullable = false)
    private String oppositeAccount;

    // 거래 상대 계좌명
    @Column(name = "loan_opposite_name", nullable = false)
    private String oppositeName;

    // 거래 후 잔액
    @Column(name = "loan_outstanding", nullable = false)
    private BigDecimal outstanding;

    // FK (대출 통장 PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_account_id", nullable = false)
    private LoanAccount loanAccount;

    // 생성자
    @Builder
    public LoanTransaction (LoanAccount loanAccount, int type, BigDecimal principal,
                            BigDecimal interest, String oppositeAccount, String oppositeName, BigDecimal outstanding) {
        this.loanAccount = loanAccount;
        this.type = type;
        this.principal = principal;
        this.interest = interest;
        this.oppositeAccount = oppositeAccount;
        this.oppositeName = oppositeName;
        this.outstanding = outstanding;
    }

}

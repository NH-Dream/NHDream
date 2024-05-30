package com.ssafy.nhdream.entity.loan;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "loan_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanAccount extends BaseTimeEntity {

    // PK
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 삭제 일자
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 대출 상태
    @Column(name = "loan_state")
    private Integer state;

    // 대출 만료 일자
    @Column(name = "expiration_at", nullable = false)
    private LocalDate expirationAt;

    // 대출 상품명
    @Column(name = "loan_product_name", nullable = false)
    private String productName;

    // 대출 계좌번호
    @Column(name = "loan_account_num", nullable = false, unique = true)
    private String accountNum;

    // 대출 원금
    @Column(name = "loan_principal", nullable = false)
    private BigDecimal principal;

    // 대출 이자
    @Column(name = "loan_interest", nullable = false)
    private BigDecimal interest;

    // 대출 원리금
    @Column(name = "loan_amount", nullable = false)
    private BigDecimal amount;

    // 대출 원금 잔액
    @Column(name = "loan_outstanding_principal", nullable = false)
    private BigDecimal outstandingPrincipal;

    // 대출 이자 잔액
    @Column(name = "loan_outstanding_interest", nullable = false)
    private BigDecimal outstandingInterest;

    // 대출 잔액
    @Column(name = "loan_balance", nullable = false)
    private BigDecimal balance;

    // 대출 이자율
    @Column(name = "loan_interest_rate", nullable = false,precision = 8, scale = 4)
    private BigDecimal interestRate;

    // 대출 계약 주소
    @Column(name = "loan_contract_address", nullable = false)
    private String contractAddress;

    // 대출 시작 일자
    @Column(name = "started_at", nullable = false)
    private LocalDate startedAt;

    // 대출 납입 회차
    @Column(name = "loan_round", nullable = false)
    private Integer round;

    // 대출 결제 방법
    @Column(name = "loan_payment_method", nullable = false)
    private Integer paymentMethod;

    // 대출 결제일
    @Column(name = "loan_payment_date", nullable = false)
    private Integer paymentDate;

    // 대출 기간(개월)
    @Column(name = "loan_term", nullable = false)
    private Integer term;

    // FK (회원 PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // FK (대출 옵션 PK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_option_id", nullable = false)
    private LoanOptions loanOptions;

    // FK (대출 심사 PK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_approval_id", nullable = false)
    private LoanApproval loanApproval;

    // 대출 거래 기록 매핑
    @OneToMany(mappedBy = "loanAccount")
    private List<LoanTransaction> loanTransactions = new ArrayList<>();


    // 생성자
    @Builder
    public LoanAccount (User user, LoanOptions loanOptions, LocalDateTime deletedAt,
                        LocalDate expirationAt, String productName, String accountNum,
                        BigDecimal principal, BigDecimal interest, BigDecimal amount,
                        BigDecimal outstandingPrincipal, BigDecimal outstandingInterest,
                        BigDecimal balance, BigDecimal interestRate, String contractAddress,
                        LocalDate startedAt, int round, int paymentMethod, int paymentDate,
                        LoanApproval loanApproval, int term, int state) {
        this.user = user;
        this.loanOptions = loanOptions;
        this.deletedAt = deletedAt;
        this.expirationAt = expirationAt;
        this.productName = productName;
        this.accountNum = accountNum;
        this.principal = principal;
        this.interest = interest;
        this.amount = amount;
        this.outstandingPrincipal = outstandingPrincipal;
        this.outstandingInterest = outstandingInterest;
        this.balance = balance;
        this.interestRate = interestRate;
        this.contractAddress = contractAddress;
        this.startedAt = startedAt;
        this.round = round;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.loanApproval = loanApproval;
        this.term = term;
        this.state = state;
    }

}

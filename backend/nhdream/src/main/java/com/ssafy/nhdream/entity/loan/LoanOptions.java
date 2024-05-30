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
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "loan_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanOptions extends BaseTimeEntity {

    // PK 값
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 이자율
    @Column(name = "loan_rate", nullable = false,precision = 8, scale = 4)
    private BigDecimal rate;

    // 대출 기간
    @Column(name = "loan_term", nullable = false)
    private int term;

    // 대출 금액
    @Column(name = "loan_amount", nullable = false)
    private BigDecimal amount;

//    // 대출 시작 일자
//    @Column(name = "started_at", nullable = false)
//    private LocalDate startedAt;
//
//    // 대출 만료 일자
//    @Column(name = "expiration_at", nullable = false)
//    private LocalDate expiratioonAt;

    // 대출 결제 방법
    @Column(name = "loan_payment_method", nullable = false)
    private int paymentMethod;

    // 대출 결제일
    @Column(name = "loan_payment_date", nullable = false)
    private int paymentDate;


    // FK (대출 상품 PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_id", nullable = false)
    private LoanProduct loanProduct;

    // FK (회원 PK)
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 대출 심사 매핑
    @OneToOne(mappedBy = "loanOptions")
    private LoanApproval loanApproval;


    // 대출 통장 매핑
    @OneToOne(mappedBy = "loanOptions")
    private LoanAccount loanAccount;

    // 생성자
    @Builder
    public LoanOptions (LoanProduct loanProduct, BigDecimal rate,
                        int term, BigDecimal amount,
                        int paymentMethod, int paymentDate, User user) {
        this.loanProduct = loanProduct;
        this.rate = rate;
        this.term = term;
        this.amount = amount;
//        this.startedAt = startedAt;
//        this.expiratioonAt = expiratioonAt;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.user = user;
    }
}

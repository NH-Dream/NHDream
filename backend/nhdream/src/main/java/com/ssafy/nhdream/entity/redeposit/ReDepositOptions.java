package com.ssafy.nhdream.entity.redeposit;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "re_deposit_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReDepositOptions {

    // 식별 ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // FK (정기예금 상품 PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "re_deposit_id")
    private ReDepositProduct reDepositProduct;

    // 이자율
    @Column(name = "re_deposit_rate", nullable = false, precision = 8, scale = 4)
    private BigDecimal rate;

    // 우대금리(수료생)
    @Column(name = "re_deposit_preferred_rate1", nullable = false, precision = 8 ,scale = 4)
    private BigDecimal preferredRate1;

    // 우대금리(귀농)
    @Column(name = "re_deposit_preferred_rate2", nullable = false, precision = 8, scale = 4)
    private BigDecimal preferredRate2;

    // 기간
    @Column(name = "re_deposit_term", nullable = false)
    private int term;

    // 생성자
    @Builder
    public ReDepositOptions(BigDecimal rate, BigDecimal preferredRate1, BigDecimal preferredRate2, int term, long amountRange, ReDepositProduct reDepositProduct) {
        this.rate = rate;
        this.preferredRate1 = preferredRate1;
        this.preferredRate2 = preferredRate2;
        this.term = term;
        this.reDepositProduct = reDepositProduct;
    }
}

package com.ssafy.nhdream.entity.loan;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
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
@Table(name = "loan_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanProduct extends BaseTimeEntity {

    // PK 값
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 삭제 일자
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 대출 상품명
    @Column(name = "loan_name", nullable = false)
    private String name;

    // 대출 최대 한도
    @Column(name = "loan_amount_range", nullable = false)
    private BigDecimal amountRange;

    // 대출 우대 금리 (교육 수료)
    @Column(name = "loan_preferred_rate1", nullable = false)
    private BigDecimal preferredRate1;

    // 대출 우대 금리 (귀농)
    @Column(name = "loan_preferred_rate2", nullable = false)
    private BigDecimal preferredRate2;

    // 대출 최소 금리
    @Column(name = "loan_min_rate", nullable = false)
    private BigDecimal minRate;

    // 대출 옵션 매핑
    @OneToMany(mappedBy = "loanProduct")
    private List<LoanOptions> options = new ArrayList<>();

    // 생성자
    @Builder
    public LoanProduct(LocalDateTime deletedAt, String name, BigDecimal amountRange, BigDecimal minRate, BigDecimal preferredRate1, BigDecimal preferredRate2) {
        this.deletedAt = deletedAt;
        this.name = name;
        this.amountRange = amountRange;
        this.minRate = minRate;
        this.preferredRate1 = preferredRate1;
        this.preferredRate2 = preferredRate2;
    }
}

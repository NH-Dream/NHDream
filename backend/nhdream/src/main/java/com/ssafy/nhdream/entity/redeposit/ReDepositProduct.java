package com.ssafy.nhdream.entity.redeposit;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
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
@Table(name = "re_deposit_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReDepositProduct extends BaseTimeEntity {

    // 식별 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // FK (정기예금 옵션)
    @OneToMany(mappedBy = "reDepositProduct")
    private List<ReDepositOptions> reDepositOptions = new ArrayList<>();

    // 해지일자
    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    // 상품명
    @Column(name = "re_deposit_name", nullable = false)
    private String name;

    // 활성상태
    @Column(name = "type", nullable = false)
    private boolean isActive = true;

    // 최대한도
    @Column(name = "re_deposit_amount_range", nullable = false)
    private BigDecimal amountRange;

    // 생성자
    @Builder
    public ReDepositProduct(String name, BigDecimal amountRange) {
        this.name = name;
        this.amountRange = amountRange;
    }

    //상품마감(활성상태 변경)
    public void updateIsActive() {
        this.isActive = false;
    }

}

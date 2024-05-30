package com.ssafy.nhdream.entity.saving;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "saving_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingProduct extends BaseTimeEntity {
    // 식별 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //삭제일자
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    //활성상태
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    //적금명
    @Column(name = "saving_name", nullable = false)
    private String name;

    //최대 월별 납입한도
    @Column(name = "saving_max_monthly_limit")
    private BigDecimal maxMonthlyLimit;

    //적금옵션매핑
    @OneToMany(mappedBy = "savingProduct")
    private List<SavingOptions> savingOptions = new ArrayList<>();

    //생성자
    @Builder
    public SavingProduct(String name, BigDecimal maxMonthlyLimit) {
        this.name = name;
        this.maxMonthlyLimit = maxMonthlyLimit;
    }

    //상품마감(활성상태 변경)
    public void updateIsActive() {
        this.isActive = false;
    }
}

package com.ssafy.nhdream.entity.saving;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "saving_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingOptions extends BaseTimeEntity {
    //식별 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //외래키
    //적금상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_product_id")
    private SavingProduct savingProduct;

    //이자율
    @Column(name = "saving_rate", nullable = false,precision = 8, scale = 4)
    private BigDecimal rate;

    //우대이자율1(수료생일떄)
    @Column(name = "saving_preferred_rate1",precision = 8,scale = 4)
    private BigDecimal preferredRate1;

    //우대이자율2(귀농)
    @Column(name = "saving_preferred_rate2",precision = 8,scale = 4)
    private BigDecimal preferredRate2;

    //기간
    @Column(name = "saving_term")
    private int term;

    //적금옵션매핑
    @OneToMany(mappedBy = "savingOptions")
    private List<SavingAccount> savingAccounts = new ArrayList<>();

    //생성자
    @Builder
    public SavingOptions(BigDecimal rate, BigDecimal preferredRate1, BigDecimal preferredRate2, int term, SavingProduct savingProduct) {
        this.rate = rate;
        this.preferredRate1 = preferredRate1;
        this.preferredRate2 = preferredRate2;
        this.term = term;
        this.savingProduct = savingProduct;
    }
}

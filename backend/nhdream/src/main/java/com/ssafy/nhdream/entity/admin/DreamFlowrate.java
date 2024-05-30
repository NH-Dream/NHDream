package com.ssafy.nhdream.entity.admin;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "dream_flowrate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DreamFlowrate extends BaseTimeEntity {

    // 식별 ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 발급량
    @Column(name = "nht_mint_amount", nullable = false)
    private int mintAmount;

    // 소각량
    @Column(name = "nht_burn_amount", nullable = false)
    private int burnAmount;

    // 마지막 블록 주소
    @Column(name = "last_block_address", nullable = false)
    private String lastBlockAddress;

    // 기준일자
    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate;

    // 생성자
    @Builder
    public DreamFlowrate(int mintAmount, int burnAmount, String lastBlockAddress, LocalDate referenceDate) {
        this.mintAmount = mintAmount;
        this.burnAmount = burnAmount;
        this.lastBlockAddress = lastBlockAddress;
        this.referenceDate = referenceDate;
    }
}

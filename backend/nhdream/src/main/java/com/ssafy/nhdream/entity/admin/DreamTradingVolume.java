package com.ssafy.nhdream.entity.admin;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "dream_trading_volume")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DreamTradingVolume extends BaseTimeEntity {

    // 식별 ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 거래량
    @Column(name = "dream_trade_amount", nullable = false)
    private int tradeAmount;

    // 마지막 블록 주소
    @Column(name = "last_block_address", nullable = false)
    private String lastBlockAddress;

    // 기준일자
    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate;

    // 생성자
    @Builder
    public DreamTradingVolume(int tradeAmount, String lastBlockAddress, LocalDate referenceDate) {
        this.tradeAmount = tradeAmount;
        this.lastBlockAddress = lastBlockAddress;
        this.referenceDate = referenceDate;
    }
}

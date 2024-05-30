package com.ssafy.nhdream.domain.redeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Schema(description = "정기예금 상품 목록 조회 반환 DTO")
public class RedepositProductListDto {

    @Schema(description = "식별 PK")
    private int id;

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "최고 이자율")
    private BigDecimal maximumRate;

    @Schema(description = "최대 한도")
    private Long maximumLimit;

    @Builder
    public RedepositProductListDto(int id, String name, BigDecimal maximumRate, BigDecimal maximumLimit) {
        this.id = id;
        this.name = name;
        this.maximumRate = maximumRate;
        this.maximumLimit = maximumLimit != null ? maximumLimit.longValue() : null;
    }
}

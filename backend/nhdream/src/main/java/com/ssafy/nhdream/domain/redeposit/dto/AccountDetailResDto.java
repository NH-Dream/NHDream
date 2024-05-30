package com.ssafy.nhdream.domain.redeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "가입 계좌 상세정보")
public class AccountDetailResDto {

    @Schema(description = "상품이름")
    private String name;

    @Schema(description = "납입금액")
    private long amount;

    @Schema(description = "적용금리")
    private double appliedInterateRate;

    @Schema(description = "만기시 예상이자")
    private long expectedInterest;

    @Schema(description = "수령액")
    private long receivedAmount;

    @Schema(description = "가입일")
    private LocalDate openDate;

    @Schema(description = "만기일")
    private LocalDate expiredAt;

    @Schema(description = "만기여부")
    private boolean isActive;
}

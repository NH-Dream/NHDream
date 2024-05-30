package com.ssafy.nhdream.domain.redeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Schema(description = "정기예금 옵션 생성 요청 DTO")
public class RedepositOptionCreationReqDto {

    @Schema(description = "기본이자율", example = "5.0")
    @DecimalMin(value = "0.0", inclusive = false, message = "기본이자율는 0보다 커야합니다.")
    private BigDecimal optionRate;

    @Schema(description = "수료생 우대금리", example = "2.0")
    @DecimalMin(value = "0.0", message = "우대금리1은 0 이상이여야 합니다.")
    private BigDecimal optionPreferredRate1;

    @Schema(description = "농부 우대금리", example = "3.0")
    @DecimalMin(value = "0.0", message = "우대금리2는 0 이상이여야 합니다.")
    private BigDecimal optionPreferredRate2;

    @Min(value = 1, message = "납입기간은 적어도 1달 이상이여야 합니다.")
    private int term;
}

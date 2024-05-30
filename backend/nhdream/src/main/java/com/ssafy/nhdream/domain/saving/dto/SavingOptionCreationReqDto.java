package com.ssafy.nhdream.domain.saving.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SavingOptionCreationReqDto {
    @DecimalMin(value = "0.0", inclusive = false, message = "기본이자율는 0보다 커야합니다.")
    private BigDecimal optionRate;

    @DecimalMin(value = "0.0", message = "우대금리1은 0 이상이여야 합니다.")
    private BigDecimal optionPreferredRate1;

    @DecimalMin(value = "0.0", message = "우대금리2는 0 이상이여야 합니다.")
    private BigDecimal optionPreferredRate2;

    @Min(value = 1, message = "납입기간은 적어도 1달 이상이여야 합니다.")
    private int term;
}

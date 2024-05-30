package com.ssafy.nhdream.domain.saving.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class SavingProductCreationReqDto {
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "최대월납입금액은 0원 초과여야합니다.")
    private BigDecimal maxMonthlyLimit;

    @NotEmpty(message = "옵션은 비어있을 수 없습니다.")
    @Valid
    private List<SavingOptionCreationReqDto> options;

}

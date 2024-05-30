package com.ssafy.nhdream.domain.saving.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SavingAccountCreationReqDto {

    @Min(value = 1,message = "OptionId 값은 1이상 정수입니다.")
    private int savingOptionId;

    @Min(value = 1,message = "기간은 1이상 정수입니다.")
    private int term;

    @DecimalMin(value = "0.0",message = "월별 납입금액은 최소 0보다 커야합니다." )
    private BigDecimal monthlyAmount;
    @Min(value = 0,message = "납입일은 1~31일 사이 정수입니다.")
    @Max(value = 31,message = "납입일은 1~31일 사이 정수입니다.")
    private int depositDate;

    @NotBlank(message = "숫자 6자리여야 합니다.")
    @Pattern(regexp = "^\\d{6}$", message = "숫자 6자리여야 합니다.")
    private String password;

}

package com.ssafy.nhdream.domain.redeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountCreationDto {

    @Min(value = 1,message = "OptionId 값은 1이상 정수입니다.")
    private int depositOptionId;

    @Min(value = 1,message = "기간은 1이상 정수입니다.")
    private int term;

    @DecimalMin(value = "0.0",message = "예금금액은 최소 0.0보다 커야합니다." )
    private BigDecimal reDepositAmount;

    @NotBlank(message = "숫자 6자리여야 합니다.")
    @Pattern(regexp = "^\\d{6}$", message = "숫자 6자리여야 합니다.")
    private String password;

}

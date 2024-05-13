package com.ssafy.nhdream.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerifyWalletPasswordReqDto {

    @NotBlank(message = "숫자 6자리여야 합니다.")
    @Pattern(regexp = "^\\d{6}$", message = "숫자 6자리여야 합니다.")
    private String walletPassword;
}

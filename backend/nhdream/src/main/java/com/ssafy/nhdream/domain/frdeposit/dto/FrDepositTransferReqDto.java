package com.ssafy.nhdream.domain.frdeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class FrDepositTransferReqDto {
    @Schema(description = "보낼돈", example = "10000")
    @DecimalMin(value = "0.00", message = "0원이하는 송금할 수 없습니다.")
    private BigDecimal amount;

    @Schema(description = "보낼 주소", example = "0xe8756cD158cefD2FE3BE082AE4D50B33Ea219daE")
    @NotBlank(message = "보낼 주소는 입력해야 합니다.")
    @Pattern(regexp = "^(0x)?[0-9a-fA-F]{40}$", message = "올바른 지갑 주소 형식이어야 합니다.")
    private String recipientWalletAddress;


    @NotBlank(message = "숫자 6자리여야 합니다.")
    @Pattern(regexp = "^\\d{6}$", message = "숫자 6자리여야 합니다.")
    private String password;
}

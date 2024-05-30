package com.ssafy.nhdream.domain.frdeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateAutomaticTransferReqDto {

    //보낼주소
    @Schema(description = "발신 주소", example = "0xe8756cD158cefD2FE3BE082AE4D50B33Ea219daE")
    @NotBlank(message = "발신할 주소는 입력해야 합니다.")
    @Pattern(regexp = "^(0x)?[0-9a-fA-F]{40}$", message = "올바른 지갑 주소 형식이어야 합니다.")
    private String recipientWalletAddress;

    //이체금액
    @Schema(description = "보낼돈", example = "10000")
    @DecimalMin(value = "0.00", message = "0원이하는 송금할 수 없습니다.")
    private BigDecimal recurringAmount;

    //입금주소
    @Schema(description = "수신 주소", example = "0xe8756cD158cefD2FE3BE082AE4D50B33Ea219daE")
    @NotBlank(message = "수신할 주소는 입력해야 합니다.")
    @Pattern(regexp = "^(0x)?[0-9a-fA-F]{40}$", message = "올바른 지갑 주소 형식이어야 합니다.")
    private String senderWalletAddress;

    //이체기간
    @Min(value = 1, message = "이체기간은 1달 이상이여야 합니다..")
    private int term;

    //매달지정일
    @Min(value = 1, message = "월 지정일은 1일 이상이여야합니다.")
    @Max(value = 31, message = "월 지정일은 31일 이하여야합니다.")
    private int transferDay;

    //지갑비번
    @NotBlank(message = "숫자 6자리여야 합니다.")
    @Pattern(regexp = "^\\d{6}$", message = "숫자 6자리여야 합니다.")
    private String walletPassword;
}

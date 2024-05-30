package com.ssafy.nhdream.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ChangeWalletPasswordReqDto {

    //기존 비번
    @Schema(description = "지갑 비밀번호", example = "123456")
    @NotBlank(message = "지갑 비밀번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^\\d{6}$",
            message ="6자리 숫자로 작성해주세요.")
    private String password;

    //새비번
    @Schema(description = "지갑 새비밀번호", example = "123456")
    @NotBlank(message = "지갑 비밀번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^\\d{6}$",
            message ="6자리 숫자로 작성해주세요.")
    private String newPassword1;

    //새비번(위에거랑 같은지 확인용)
    @Schema(description = "지갑 새비밀번호확인용", example = "123456")
    @NotBlank(message = "지갑 비밀번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^\\d{6}$",
            message ="6자리 숫자로 작성해주세요.")
    private String newPassword2;
}

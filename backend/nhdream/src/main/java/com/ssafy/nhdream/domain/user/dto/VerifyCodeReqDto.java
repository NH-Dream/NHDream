package com.ssafy.nhdream.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerifyCodeReqDto {

    @Schema(description = "인증 받은 이메일", example = "bonheur9813@naver.com")
    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    @Email(message = "이메일 형식을 확인해주세요.")
    private String email;

    @Schema(description = "받은 인증 코드", example = "123456")
    @NotBlank(message = "인증코드는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^\\d{6}$",
            message ="6자리 숫자로 작성해주세요.")
    private String authCode;
}

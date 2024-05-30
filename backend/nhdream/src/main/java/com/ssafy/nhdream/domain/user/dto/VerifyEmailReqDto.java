package com.ssafy.nhdream.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailReqDto {

    @Schema(description = "코드 받을 이메일", example = "bonheur9813@naver.com")
    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    @Email(message = "이메일 형식을 확인해주세요.")
    private String email;

    @Schema(description = "아이디", example = "ssafy1")
    @NotBlank(message = "아이디는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$",
            message = "시작은 영문으로만, '_'를 제외한 특수문자 안되며 명문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하여야 합니다.")
    private String loginId;
}

package com.ssafy.nhdream.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
public class JoinReqDto {

    @Schema(description = "아이디", example = "ssafy1")
    @NotBlank(message = "아이디는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$",
            message = "시작은 영문으로만, '_'를 제외한 특수문자 안되며 명문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하여야 합니다.")
    private String loginId;

    @Schema(description = "비밀번호", example = "nhdream1!")
    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
            message = "숫자, 문자, 특수문자 포함 8~15자리 이내여야 합니다.")
    private String password;

    @Schema(description = "이름", example = "김싸피")
    @NotBlank(message = "이름은 필수로 입력해야 합니다.")
    @Pattern(regexp = "^[가-힣]{2,}$", message ="2자리 이상 한글로 작성해주세요.")
    private String name;

    @Schema(description = "이메일", example = "ssafy1@naver.com")
    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    @Email(message = "이메일 형식을 확인해주세요.")
    private String email;

    @Schema(description = "전화번호", example = "01012341234")
    @NotBlank(message = "전화번호은 필수로 입력해야 합니다.")
    @Pattern(regexp = "^(01[016789]{1})-?[0-9]{3,4}-?[0-9]{4}$", message ="2자리 이상 한글로 작성해주세요.")
    private String phone;

    @Schema(description = "생년월일", example = "2020-12-12")
//  @Past(message = "과거 날짜만 입력하세요.")
    private LocalDate birthDate;

    @Schema(description = "지갑 비밀번호", example = "123456")
    @NotBlank(message = "지갑 비밀번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^\\d{6}$",
            message ="6자리 숫자로 작성해주세요.")
    private String walletPassword;

}

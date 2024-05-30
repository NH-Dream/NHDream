package com.ssafy.nhdream.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ApplyFarmerReqDto {

    @Schema(description = "사업자 등록번호", example = "0123456789")
    @Digits(integer = 10, fraction = 0, message = "10자리의 숫자만 입력하세요.")
    private long licenseNum;

    @Schema(description = "대표자 성명", example = "김싸피")
    @NotBlank(message = "이름은 필수로 입력해야 합니다.")
    @Pattern(regexp = "^[가-힣]{2,}$", message ="2자리 이상 한글로 작성해주세요.")
    private String representative;

    @Schema(description = "개업일자", example = "2020-02-01")
    @Past(message = "과거 날짜만 입력하세요.")
    private LocalDate registrationDate;

    @Schema(description = "신청자 아이디", example = "ssafy1")
    private String loginId;

}

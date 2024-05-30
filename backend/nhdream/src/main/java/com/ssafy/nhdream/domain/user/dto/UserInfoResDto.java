package com.ssafy.nhdream.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "개인 정보 조회 응답 DTO")
public class UserInfoResDto {
    @Schema(description = "이름")
    private String name;

    @Schema(description = "생년월일")
    private LocalDate birthDay;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "아이디")
    private String loginId;

    @Schema(description = "유저 타입")
    private String type;
}

package com.ssafy.nhdream.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "교육 인증 승인 요청 DTO")
public class ReviewReqDto {

    @Schema(description = "신청자 PK")
    private int userId;

    @Schema(description = "신청 게시물 PK")
    private int eduApplyId;

    @Schema(description = "심사 결과")
    private int result;

}

package com.ssafy.nhdream.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "대출 인증 승인 요청 DTO")
public class LoanReviewReqDto {

    @Schema(description = "대출 신청 PK")
    private int applyPostId;

    @Schema(description = "심사 결과")
    private int result;
}

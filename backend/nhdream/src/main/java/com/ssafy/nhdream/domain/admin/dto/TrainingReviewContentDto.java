package com.ssafy.nhdream.domain.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TrainingReviewContentDto {

    // 현재 컨텐츠 번호
    private int curReviewCnt;

    // 교육 심사 PK
    private int trainingReviewId;

    // 신청자명
    private String userName;

    // 교육명
    private String trainingName;

    // 신청일자
    private LocalDateTime applicationDate;

    // 심사 상태
    private ApprovalStatus approvalStatus;

    @QueryProjection
    public TrainingReviewContentDto(int trainingReviewId, String userName, String trainingName, LocalDateTime applicationDate, ApprovalStatus approvalStatus) {
        this.trainingReviewId = trainingReviewId;
        this.userName = userName;
        this.trainingName = trainingName;
        this.applicationDate = applicationDate;
        this.approvalStatus = approvalStatus;
    }
}

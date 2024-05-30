package com.ssafy.nhdream.entity.admin;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type",nullable = false)
    private JopType jobType;


    @Column(name = "is_success", nullable = false)
    private boolean isSuccess;

    @Column(name = "is_processed", nullable = false)
    private boolean isProcessed;

    @Column(name = "error_type", nullable = false)
    private String errorType;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @Column(name = "error_detail", nullable = false, length = 1000)
    private String errorDetail;

    @Builder
    TaskLog(boolean isSuccess,JopType jobType, boolean isProcessed, String errorType, String errorMessage, String errorDetail) {
        this.jobType = jobType;
        this.isSuccess = isSuccess;
        this.isProcessed = isProcessed;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }

    public void updateIsSuccess(boolean state) {
        this.isSuccess = state;
    }

    public void updateIsProcessed(boolean state) {
        this.isProcessed = state;
    }
}

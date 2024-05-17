package com.ssafy.nhdream.domain.admin.dto;

import com.ssafy.nhdream.entity.admin.JopType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LogDetailResDto {
    private int id;
    private JopType jopType;
    private boolean isSuccess;
    private boolean isProcessed;
    private String errorType;
    private String errorMessage;
    private String errorDetail;
}

package com.ssafy.nhdream.domain.frdeposit.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class PatchAutoTransferReqDto {

    //이체돈
    @DecimalMin(value = "0.01", message = "이체량은 0보다 커야합니다.")
    private BigDecimal amount;

    @Min(value = 1, message = "월 지정일은 1일 이상이여야합니다.")
    @Max(value = 31, message = "월 지정일은 31일 이하여야합니다.")
    private int transferDate;

    @Future(message = "만기일은 최소 오늘 이후여야 합니다.")
    private LocalDate expiredAt;
}

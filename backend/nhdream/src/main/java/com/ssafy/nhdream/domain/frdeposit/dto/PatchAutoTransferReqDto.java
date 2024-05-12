package com.ssafy.nhdream.domain.frdeposit.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class PatchAutoTransferReqDto {

    //이체돈
    private BigDecimal amount;

    private int transferDate;

    private LocalDate expiredAt;
}

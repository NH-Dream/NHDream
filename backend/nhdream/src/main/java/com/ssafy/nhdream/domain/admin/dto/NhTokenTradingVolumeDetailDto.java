package com.ssafy.nhdream.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NhTokenTradingVolumeDetailDto {
    private int id;
    private long tradeAmount;
    private LocalDate referenceDate;
}

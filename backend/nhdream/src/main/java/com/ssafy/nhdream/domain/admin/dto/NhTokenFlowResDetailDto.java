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
public class NhTokenFlowResDetailDto {
    private int id;
    private LocalDate referenceDate;
    private long mintAmount;
    private long burnAmount;
}

package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class MyLoanListDto {

    private int id;
    private String name;
    private LocalDateTime date;
    private BigDecimal amount;
    private int approval;

}

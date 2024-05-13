package com.ssafy.nhdream.domain.loan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LoanListDto {

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private BigDecimal minRate;
    @NotBlank
    private BigDecimal maxRate;
    @NotBlank
    private BigDecimal amountRange;

}

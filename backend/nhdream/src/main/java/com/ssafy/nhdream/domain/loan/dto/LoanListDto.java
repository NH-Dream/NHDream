package com.ssafy.nhdream.domain.loan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LoanListDto {

    @NotBlank
    private String title;
    @NotBlank
    private BigDecimal minRate;
    @NotBlank
    private BigDecimal maxRate;
    @NotBlank
    private BigDecimal amountRange;

}

package com.ssafy.nhdream.domain.saving.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SavingProductListDto {

    private int id;
    private String name;
    private double maxInterestRate;
    private int maxMonthlyLimit;

}

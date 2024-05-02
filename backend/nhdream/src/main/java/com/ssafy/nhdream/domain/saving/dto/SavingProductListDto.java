package com.ssafy.nhdream.saving.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class SavingProductListDto {

    private int id;
    private String name;
    private double maxInterestRate;
    private int maxMonthlyLimit;

}

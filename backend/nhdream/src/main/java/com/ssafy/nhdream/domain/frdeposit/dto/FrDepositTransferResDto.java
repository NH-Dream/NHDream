package com.ssafy.nhdream.domain.frdeposit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FrDepositTransferResDto {
    private BigDecimal remainingBalance;
}
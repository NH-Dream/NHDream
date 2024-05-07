package com.ssafy.nhdream.domain.frdeposit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CheckFrDepositAccountNameResDto {
    private String oppositeAccountNum;
    private String oppositeName;
    private String oppositeContractAddress;
}

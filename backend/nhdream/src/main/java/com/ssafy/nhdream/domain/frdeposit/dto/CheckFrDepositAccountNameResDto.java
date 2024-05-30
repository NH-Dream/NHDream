package com.ssafy.nhdream.domain.frdeposit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckFrDepositAccountNameResDto {
    private String oppositeAccountNum;
    private String oppositeName;
    private String oppositeContractAddress;

    //쿼리스트링 있을 때 시작일도 반환
    private LocalDate startDate;
}

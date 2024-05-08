package com.ssafy.nhdream.domain.loan.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoanListResDto {

    List<LoanListDto> loanListDtos;

}

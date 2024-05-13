package com.ssafy.nhdream.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TransactionListResDto {

    List<TransactionDto> transactionDtos;

}

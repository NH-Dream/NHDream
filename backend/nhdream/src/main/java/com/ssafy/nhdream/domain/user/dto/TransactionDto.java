package com.ssafy.nhdream.domain.user.dto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionDto implements Comparable<TransactionDto> {

    // 거래 날짜, 거래 상품 명, 거래 계좌번호(나 말고 상대), 거래 금액, 거래 후 잔액, 입금인지 출금인지 정보
    // 시간 순서대로 정리하기
    private LocalDateTime tansactionDate;
    private String productName;
    private String accountNum;
    private BigDecimal amount;
    private BigDecimal outstanding;
    private int transactionType;
    private String accountType;

    @Override
    public int compareTo(TransactionDto transactionDto) {
        return this.tansactionDate.compareTo(transactionDto.tansactionDate);
    }

}

package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class LoanOptionReqDto {

    // 대출상품id, 대출 금액, 대출 시작 일자, 대출 만료 일자, 사용자 유형, 상환 방법(대출 결제 방식), 대출 금리, 대출 결제일, 대출 기간
    private int loanProductId;
    private BigDecimal amount;
    private int userId;
    private int paymentMethod;
    private int paymentDate;
    private int term;


}

package com.ssafy.nhdream.domain.admin.dto;

import com.ssafy.nhdream.entity.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class LoanReviewDetailResDto {

    private String userName;

    private String email;

    private String phone;

    private LocalDate birthDate;

    private String userType;

    private String nhAmount;

    private String productName;

    private BigDecimal loanAmount;

    private String paymentMethod;

    private int term;

    private BigDecimal rate;

    private String totalPayments;

    private String copyOfIdCard;

    private String farmlandCert;

    private String incomeCert;

    private String nationalTaxCert;

    private String localTaxCert;

}

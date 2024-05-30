package com.ssafy.nhdream.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateInterest {

    //적금 해당월 이자액수 구하는 함수
    public static BigDecimal calculateMonthlyInterest(BigDecimal balance, BigDecimal annualInterestRate) {
        //연이자를 나눠서 소숫점2자리까지 반올림해서 월이자로 변환
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 4, RoundingMode.DOWN);
        //월이자에 1더하기 1.이자율이 되야 이자 구해지니까
        monthlyInterestRate = monthlyInterestRate.add(BigDecimal.valueOf(1));
        //잔액에 이자율 곱하기
        BigDecimal monthlyInterest = balance.multiply(monthlyInterestRate);
        //올림을 통해 정수로 만들고 반환
        return monthlyInterest.setScale(0, RoundingMode.UP);
    }

    //적금 만기시 총 이자 계산
    public static BigDecimal calculateTotalSavingInterest(BigDecimal monthlyAmount, BigDecimal annualInterestRate, int term) {
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 4, RoundingMode.DOWN);
        BigDecimal totalInterest = BigDecimal.ZERO;
        for (int i = 0; i < term; i++) {
            BigDecimal monthlyInterest = monthlyAmount.multiply(monthlyInterestRate).multiply(BigDecimal.valueOf(i+1));
            totalInterest = totalInterest.add(monthlyInterest);
        }
        return totalInterest.setScale(0, RoundingMode.UP);
    }
}

package com.ssafy.nhdream.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CalculateDate {

    //현재시간으로 부터 만기일까지의 타임스탬프 계산하는 함수(솔리디티 인자로 넣기용)
    public static BigInteger GetSecondsBetweenNowAndMaturityDate(int term, LocalDate startDate) {
        //현재시간
        LocalDateTime currentDateTime = LocalDateTime.now();

        //x기간 후 시간
        LocalDateTime maturityDateTime = startDate.atStartOfDay().plusMonths(term).toLocalDate().atStartOfDay();

        //현재 시간과 만기까지의 시간
        long differenceInSeconds = ChronoUnit.SECONDS.between(currentDateTime, maturityDateTime);

        return BigInteger.valueOf(differenceInSeconds);
    }

    //만료일 구하는 함수
    public static LocalDate GetMaturityDateWithTerm(LocalDate startDay, int term) {
        return startDay.plusMonths(term).minusDays(1);
    }


    //현재 날짜를 바탕으로 가장 가까운 유효한 지정 일자를 찾는 함수
    public static LocalDate findNextPaymentDate(int PaymentDate) {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();

        if (PaymentDate >= currentDay) {
            try {
                return currentDate.withDayOfMonth(PaymentDate);
            } catch (DateTimeException e) {
                return nextValidDate(currentDate, PaymentDate);
            }
        } else {
            return nextValidDate(currentDate.plusMonths(1), PaymentDate);
        }
    }

    //해당 일자가 없는 날이면 해당월의 마지막날을 반환하는 함수(31,30일 등 없는 날이 납입일이면 처리하는 함수)
    public static LocalDate nextValidDate(LocalDate date, int PaymentDate) {
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        if (PaymentDate > lastDayOfMonth.getDayOfMonth()) {
            return lastDayOfMonth;
        } else {
            return date.withDayOfMonth(PaymentDate);
        }
    }

    //오늘 날짜 넣으면 20240507처럼 숫자로 나오게 하는 함수(솔리디티 인자넣기용)
    public static BigInteger changeLocalDatetoBigInteger(LocalDate currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);
        return new BigInteger(formattedDate);
    }

    public static String changeDateTimeFormat(LocalDate localDate) {
        // LocalDate 포맷터 생성 ("yyyy-MM" 형식)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");


        return localDate.format(formatter);
    }

}

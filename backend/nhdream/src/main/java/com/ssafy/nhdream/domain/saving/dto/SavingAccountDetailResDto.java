package com.ssafy.nhdream.domain.saving.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SavingAccountDetailResDto {
    //상품이름
    private String name;
    //잔액
    private long balance;
    //계좌번호
    private String accountNum;
    //적용금리
    private int interestRate;
    //신규가입일
    private LocalDate joinDate;
    //만기일
    private LocalDate expiredAt;
    //1회적립금액
    private long monthlyAmount;
    //만기여부(0이면 진행중 1이면 만기)
    private int type;
}

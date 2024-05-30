package com.ssafy.nhdream.domain.frdeposit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetAutomaticTransferLogResDto {
    //id
    private int id;

    //받는주소
    private String recipientWalletAddress;

    //받는사람이름
    private String recipientName;

    //이체금액
    private long amount;

    //이체시작일
    private LocalDate startedAt;

    //이체마감일
    private LocalDate expiredAt;

    //자동이체일
    private int transferDay;

    //다음자동이체예정일
    private LocalDate nextScheduledTransferDate;

    //거래타입(0이면 자유입출금, 1이면 자유-> 적금, 2이면 자유 -> 대출)
    private int type;
}

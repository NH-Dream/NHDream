package com.ssafy.nhdream.domain.voucher.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class BuyVoucherItemReqDto {

    private String loginId;
    private int itemId;
    private int amount;
    private int usingNHDC;
    private int usingDRDC;

}

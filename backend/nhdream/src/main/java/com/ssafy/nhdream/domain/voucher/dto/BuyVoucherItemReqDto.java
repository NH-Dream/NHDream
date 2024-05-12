package com.ssafy.nhdream.domain.voucher.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class BuyVoucherItemReqDto {

    private int walletAddress;
    private int itemId;
    private int amount;
    private BigDecimal usingNHDC;
    private BigDecimal usingDRDC;

}

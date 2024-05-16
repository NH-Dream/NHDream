package com.ssafy.nhdream.domain.voucher.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Slf4j
public class VoucherTokenResDto {
    private BigInteger amount;

    public VoucherTokenResDto(BigInteger amount) {
        this.amount = amount;
    }
}

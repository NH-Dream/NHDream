package com.ssafy.nhdream.domain.voucher.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class VoucherAdminResDto {

    List<BigInteger> mintedList;
    List<BigInteger> burnedList;

    public VoucherAdminResDto(List<BigInteger> mintedList, List<BigInteger> burnedList) {
        this.mintedList = mintedList;
        this.burnedList = burnedList;
    }
}

package com.ssafy.nhdream.domain.voucher.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AffiliateListResDto {

    private String name;

    private String walletAddress;

    private String walletPrivateKey;

    private LocalDateTime createdAt;

    public AffiliateListResDto(String name, String walletAddress, String walletPrivateKey, LocalDateTime createdAt) {
        this.name = name;
        this.walletAddress = walletAddress;
        this.walletPrivateKey = walletPrivateKey;
        this.createdAt = createdAt;
    }
}

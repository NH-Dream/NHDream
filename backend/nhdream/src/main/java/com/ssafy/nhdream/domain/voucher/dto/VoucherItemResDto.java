package com.ssafy.nhdream.domain.voucher.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VoucherItemResDto {

    private Integer id;
    private String title;
    private LocalDateTime createdAt;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;
    private int type;
    private String affiliateName;

    public VoucherItemResDto(Integer id, String title, LocalDateTime createdAt, BigDecimal price, int quantity, String imageUrl, int type, String affiliateName) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.type = type;
        this.affiliateName = affiliateName;
    }
}

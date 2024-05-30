package com.ssafy.nhdream.domain.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateVoucherItemReqDto {

    @Schema(description = "바우처 Id", example = "1")
    @NotBlank(message = "바우처 Id은 필수로 입력해야 합니다.")
    private Integer affiliateId;

    @Schema(description = "상품명", example = "벌레들 다 잡는 XX비료")
    @NotBlank(message = "상품명은 필수로 입력해야 합니다.")
    private String name;

    @Schema(description = "가격", example = "220000")
    @NotNull(message = "가격은 필수로 입력해야 합니다.")
    private BigDecimal price;

    @Schema(description = "수량", example = "200")
    @NotNull(message = "수량은 필수로 입력해야 합니다.")
    private int quantity;

    @Schema(description = "카테고리", example = "1")
    @NotNull(message = "카테고리는 필수로 입력해야 합니다.")
    private int type;

}

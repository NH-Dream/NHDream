package com.ssafy.nhdream.domain.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ModifyVoucherItemReqDto {

    @Schema(description = "상품 ID", example = "1")
    @NotBlank(message = "상품 pk은 필수로 입력해야 합니다.")
    private Integer id;

    @Schema(description = "상품명", example = "벌레들 다 잡는 XX비료")
    @NotBlank(message = "상품명은 필수로 입력해야 합니다.")
    private String name;

    @Schema(description = "가격", example = "220000")
    @NotBlank(message = "가격은 필수로 입력해야 합니다.")
    private BigDecimal price;

    @Schema(description = "수량", example = "200")
    @NotBlank(message = "수량은 필수로 입력해야 합니다.")
    private int quantity;

    @Schema(description = "상품 타입", example = "1")
    private int type;

}

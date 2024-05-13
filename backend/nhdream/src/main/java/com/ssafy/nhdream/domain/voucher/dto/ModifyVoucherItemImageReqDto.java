package com.ssafy.nhdream.domain.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyVoucherItemImageReqDto {

    @Schema(description = "이미지를 변경할 상품 Id", example = "1")
    @NotBlank(message = "이미지를 변경할 상품의 pk는 필수입니다.")
    private Integer id;
}

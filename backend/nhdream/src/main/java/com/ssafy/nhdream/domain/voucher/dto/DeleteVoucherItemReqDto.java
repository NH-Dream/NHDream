package com.ssafy.nhdream.domain.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DeleteVoucherItemReqDto {

    @Schema(description = "삭제하려는 상품 ID", example = "1")
    @NotBlank(message = "상품 pk는 필수로 입력해야 합니다.")
    private Integer id;

}

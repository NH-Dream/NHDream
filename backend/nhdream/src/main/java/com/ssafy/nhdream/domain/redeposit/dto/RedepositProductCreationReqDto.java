package com.ssafy.nhdream.domain.redeposit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Schema(description = "정기예금 상품 생성 요청 DTO")
public class RedepositProductCreationReqDto {
    @Schema(description = "상품명", example = "함께하는 미래농부예금")
    @NotBlank(message = "상품명은 공백일 수 없습니다.")
    private String name;

    @Schema(description = "예금 최대 한도", example = "20000000")
    @DecimalMin(value = "0.0", inclusive = false, message = "최대한도는 0원 초과여야합니다.")
    private BigDecimal maximum;

    @Schema(description = "옵션")
    @NotEmpty(message = "옵션은 비어있을 수 없습니다.")
    @Valid
    private List<RedepositOptionCreationReqDto> options;
}

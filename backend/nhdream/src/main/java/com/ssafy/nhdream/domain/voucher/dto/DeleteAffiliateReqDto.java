package com.ssafy.nhdream.domain.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class DeleteAffiliateReqDto {

    @Schema(description = "삭제할 바우처 ID", example = "1")
    @NotBlank(message = "삭제할 바우처의 ID는 필수입니다.")
    @Pattern(regexp = "^\\d$", message = "바우처의 올바른 id 값을 넣어주세요.")
    private int id;

}

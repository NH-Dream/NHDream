package com.ssafy.nhdream.domain.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class CreateAffiliateReqDto {

    @Schema(description = "바우처 이름", example = "최강 비료")
    @NotBlank(message = "바우처명은 필수로 입력해야 합니다.")
    private String name;

    // 바우처를 추가하면서 지갑을 같이 만들어 주는게 맞는지 확인

//    @Schema(description = "바우처 지갑 주소", example = "20911100001")
//    @NotBlank(message = "바우처 지갑 주소는 필수로 입력해야 합니다.")
//    private String affiliateAccountNum;

}

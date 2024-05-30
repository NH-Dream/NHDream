package com.ssafy.nhdream.domain.saving.controller;

import com.ssafy.nhdream.common.auth.CustomUserDetails;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.saving.dto.*;
import com.ssafy.nhdream.domain.saving.service.SavingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/savings")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "2.Saving", description = "SAVING API")
public class SavingController {
    private final SavingService savingService;
    private final NHDCService nhdcService;

    //적금상품목록 조회
    @Operation(summary = "적금상품목록조회", description = "/savings\n\n 적금상품목록 조회")
    @GetMapping("")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getSavingProductList() {
        List<SavingProductListResDto> savingProductLists = savingService.getSavingProductList();
        log.info("적금상품목록 조회 API 호출");
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, savingProductLists));
    }

    //적금상품생성
    @Operation(summary = "적금상품생성", description = "/savings\n\n 적금상품생성")
    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> createSavingProduct(@RequestBody @Validated SavingProductCreationReqDto savingProductCreationReqDto) {

        //관리자인지는 시큐리티단에서 처리한다해서 고려하지 않음
        int savedSavingAccountId = savingService.createSavingProduct(savingProductCreationReqDto);
        log.info(" 적금상품생성 savingProductId = {}", savedSavingAccountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, savedSavingAccountId));
    }

    //적금상세보기
    @Operation(summary = "적금상품상세보기", description = "/savings/{savingProductId}\n\n 적금상품상세보기")
    @GetMapping("/{savingProductId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getSavingProductDetail(@PathVariable int savingProductId) {
        SavingProductDetailResDto savingProductDetailResDto = savingService.getSavingProductDetail(savingProductId);
        log.info("{} : 적금 상품 상세 보기 실행", savingProductId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, savingProductDetailResDto));
    }

    //적금계좌생성
    @Operation(summary = "적금계좌생성", description = "/savings/{savingProductId}\n\n 적금계좌생성")
    @PostMapping("/{savingProductId}")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> createSavingAccount(@RequestBody @Validated SavingAccountCreationReqDto savingAccountCreationReqDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int savedSavingAccountId = savingService.createSavingAccount(savingAccountCreationReqDto, userDetails.getId());
        log.info("적금계좌생성 userId: {}, savingAccountId: {}", userDetails.getId(), savedSavingAccountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, savedSavingAccountId));
    }

    //적금계좌 거래내역 조회
    @Operation(summary = "적금계좌거래내역조회", description = "/savings/{savingAccountId}/transactions\n\n 적금계좌거래내역조회")
    @GetMapping("/{savingAccountId}/transactions")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getSavingAccountTransacions(@PathVariable int savingAccountId,
                                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                  Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        SavingHistoryResDto savingHistoryResDto = savingService.getSavingHistory(savingAccountId, userDetails.getId(), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, savingHistoryResDto));
    }

    //적금계좌 상세조회
    @Operation(summary = "적금계좌상세조회", description = "/savings/{savingAccountId}/detail\n\n 적금계좌상세조회")
    @GetMapping("/{savingAccountId}/detail")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getSavingAccountDetail(@PathVariable int savingAccountId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        SavingAccountDetailResDto savingAccountDetailResDto = savingService.getSavingAccountDetail(savingAccountId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, savingAccountDetailResDto));
    }



    @Operation(summary = "테스트용", description = "/savings/test\n\n 테스트")
    @GetMapping("/test")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> testtestetsetest() {
        nhdcService.getMintEventLogs();
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, 0));
    }

}

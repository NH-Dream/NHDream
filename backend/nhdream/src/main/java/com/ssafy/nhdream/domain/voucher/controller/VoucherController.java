package com.ssafy.nhdream.domain.voucher.controller;

import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.voucher.dto.*;
import com.ssafy.nhdream.domain.voucher.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "5. VOUCHER", description = "VOUCHER API")
public class VoucherController {

    private final VoucherService voucherService;

    @Operation(summary = "바우처 생성", description = "/vouchers\n\n 바우처 생성")
    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> createVoucher(@RequestBody @Valid CreateAffiliateReqDto createAffiliateDto) {
        voucherService.createAffiliate(createAffiliateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "바우처 수정", description = "/vouchers\n\n 바우처 수정")
    @PatchMapping("")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> modifyVoucher(@RequestBody @Valid ModifyAffiliateReqDto modifyAffiliateDto) {
        voucherService.modifyAffiliate(modifyAffiliateDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "바우처 삭제", description = "/vouchers\n\n 바우처 삭제")
    @DeleteMapping("")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> deleteVoucher(@RequestBody @Valid DeleteAffiliateReqDto deleteAffiliateDto) {
        voucherService.deleteAffiliate(deleteAffiliateDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "바우처 목록", description = "/vouchers\n\n 바우처 목록 반환")
    @GetMapping("")
    @ApiResponse(responseCode = "200", description = "성공 \n\n VoucherAffiliate 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getVoucherList() {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, voucherService.getAffiliateList()));
    }

    @Operation(summary = "바우처 상품 생성", description = "/vouchers/items\n\n 바우처 상품 생성")
    @PostMapping(value = "/items", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> createVoucherItem(
            @RequestPart @Valid CreateVoucherItemReqDto createVoucherItemReqDto,
            @RequestPart(name = "itemImage") MultipartFile itemImage) {
        voucherService.createItem(createVoucherItemReqDto, itemImage);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "바우처 상품 수정", description = "/vouchers/items\n\n 바우처 상품 수정")
    @PatchMapping(value = "/items")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> modifyVoucherItem(@RequestPart @Valid ModifyVoucherItemReqDto modifyVoucherItemReqDto) {
        voucherService.modifyItem(modifyVoucherItemReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "바우처 상품 이미지 수정", description = "/vouchers/items/image\n\n 바우처 상품 이미지 수정")
    @PatchMapping(value= "/items/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> modifyItemImage(
            @RequestPart @Valid ModifyVoucherItemImageReqDto modifyVoucherItemImageReqDto,
            @RequestPart(value = "itemImage") MultipartFile itemImage) {
        voucherService.modifyItemImage(modifyVoucherItemImageReqDto, itemImage);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "바우처 상품 삭제", description = "/vouchers/items\n\n 바우처 상품 삭제")
    @DeleteMapping("/items")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> deleteVoucherItem(@RequestBody @Valid DeleteVoucherItemReqDto deleteVoucherItemReqDto) {
        voucherService.deleteItem(deleteVoucherItemReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "바우처 상품 목록 조회", description = "/vouchers/items\n\n VoucherItem List 조회")
    @GetMapping("/items")
    @ApiResponse(responseCode = "200", description = "성공 \n\n VoucherItemDTO 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getVoucherItemList() {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, voucherService.getVoucherItemList()));
    }

    @Operation(summary = "바우처 카테고리별 상품 목록 조회", description = "/vouchers/items?type=\n\n 카테고리별 VoucherItem List 조회")
    @GetMapping(value = "/items", params = "type")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 카테고리별 VoucherItemDTO 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getVoucherItemListByCategory(@RequestParam("type") int type) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, voucherService.getVoucherItemListByCategory(type)));
    }

    @Operation(summary = "바우처 상품 상세 조회", description = "/vouchers/items/{itemId}\n\n 바우처 상품 ")
    @GetMapping("/items/{itemId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n VoucherItemDto 반환")
    public ResponseEntity<? extends BaseResponseBody> getVoucherItem(@PathVariable int itemId) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, voucherService.getVoucherItem(itemId)));
    }

    @Operation(summary = "바우처 토큰 잔액 조회", description = "/vouchers/token/{walletAddress}\n\n 바우처 토큰 잔액 조회")
    @GetMapping("/token/{loginId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n DRDC 잔액 반환")
    public ResponseEntity<? extends BaseResponseBody> getVoucherTokenBalance(@PathVariable String loginId) {
        // DRDC Service 생성해서 리턴해야 함.
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, voucherService.getVoucherToken(loginId)));
    }

    @Operation(summary = "바우처 상품 구매", description = "/vouchers/items/buy\n\n 바우처 상품 구매 ")
    @PostMapping("/buy")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> buyVoucherItem(@RequestBody BuyVoucherItemReqDto buyVoucherItemReqDto) {
        voucherService.buyVoucherItem(buyVoucherItemReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "관리자 DRDC 일자별 통계", description = "/vouchers/admin\n\n 관리자 DRDC 통계 페이지에서 호출 ")
    @GetMapping("/admin")
    @ApiResponse(responseCode = "200", description = "성공 \n\n VoucherAdminResDto 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getVoucherDRDCByDate(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, voucherService.getVoucherDRDCByDate(fromDate, toDate)));
    }

}

package com.ssafy.nhdream.domain.redeposit.controller;

import com.ssafy.nhdream.common.auth.CustomUserDetails;
import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.redeposit.dto.*;
import com.ssafy.nhdream.domain.redeposit.service.RedepositService;
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

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/redeposit")
@Tag(name = "6.Re-deposit", description = "RE-DEPOSIT API")
public class RedepositController {

    private final RedepositService redepositService;

    @Operation(summary = "정기예금상품생성", description = "/redeposit\n\n 정기예금상품생성")
    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> createRedepositProduct(@RequestBody @Validated RedepositProductCreationReqDto redepositProductCreationReqDto) {

        log.info("상품명 : {}", redepositProductCreationReqDto.getName());
        log.info("최대 한도 : {}", redepositProductCreationReqDto.getMaximum());
        for(RedepositOptionCreationReqDto r : redepositProductCreationReqDto.getOptions()){
            log.info("기간 : {}", r.getTerm());
            log.info("기본이율 : {}", r.getOptionRate());
            log.info("우대금리1 : {}", r.getOptionPreferredRate2());
            log.info("우대금리2 : {}", r.getOptionPreferredRate2());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, redepositService.createRedepositProduct(redepositProductCreationReqDto)));
    }

    @Operation(summary = "정기예금 상품 목록조회", description = "/redeposit\n\n 정기예금 상품 목록조회")
    @GetMapping("")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 상품명, 금액한도, 최대연이자율이 담긴 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getRedepositProductList() {


        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, redepositService.getRedepositProductList()));
    }

    @Operation(summary = "정기예금 상품 상세조회", description = "/savings\n\n 정기예금 상품 상세조회")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 상품명, 금액한도, 최대연이자율이 담긴 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getRedepositProductDetail(@PathVariable(name = "id") int id) {

        log.info("{}", id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, redepositService.getRedepositProductDetail(id)));
    }

    @Operation(summary = "정기예금 가입", description = "/redeposit\n\n 정기예금가입")
    @PostMapping("/{depositProductId}")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> openRedepositAccount(@RequestBody @Validated AccountCreationDto accountCreationDto,
                                                                           @PathVariable(name = "depositProductId") int depositProductId,
                                                                           Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        log.info("Controller start : {}", accountCreationDto.getReDepositAmount());
        redepositService.openAccount(accountCreationDto, userDetails.getId(), depositProductId);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "가입한 정기예금계좌 상세정보", description = "/redeposit\n\n 정기예금가입")
    @GetMapping("/detail/{depositAccountId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getMyAccountDetail(@PathVariable(name = "depositAccountId") int depositAccountId,
                                                                         Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        AccountDetailResDto myAccountDetail = redepositService.getMyAccountDetail(depositAccountId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, myAccountDetail));
    }

    @Operation(summary = "가입한 정기예금계좌 거래 내역 조회", description = "/redeposit\n\n 가입한 정기예금계좌 거래 내역 조회")
    @GetMapping("/transfer/{depositAccountId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getMyAccountTransfer(@PathVariable(name = "depositAccountId") int depositAccountId,
                                                                         Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        AccountHistoryResDto accountTransfer = redepositService.getMyAccountTransfer(depositAccountId, userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, accountTransfer));
    }


}

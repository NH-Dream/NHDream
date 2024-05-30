package com.ssafy.nhdream.domain.frdeposit.controller;

import com.ssafy.nhdream.common.auth.CustomUserDetails;
import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.frdeposit.dto.*;
import com.ssafy.nhdream.domain.frdeposit.service.FrDepositService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "3.FrDeposit(Wallet)", description = "FrDeposit API")
public class FrDepositController {
    private final FrDepositService frDepositService;

    //상대 계좌명 조회
    @Operation(summary = "자유입출금계좌주명조회", description = "/wallets \n\n 자유입출금계좌주명 조회")
    @GetMapping("/{walletAddress}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getOppositeName(@PathVariable String walletAddress,
                                                                      @RequestParam(required = false) Integer transferDate) {

        CheckFrDepositAccountNameResDto checkFrDepositAccountNameResDto;
        if (transferDate == null)
        {
            checkFrDepositAccountNameResDto = frDepositService.checkOppositeName(walletAddress);
            log.info("{}자유입출금계좌 계좌주명 조회", walletAddress);
        } else {
            checkFrDepositAccountNameResDto = frDepositService.checkOppositeNameAndStartDate(walletAddress, transferDate);
            log.info("{}자유입출금계좌 계좌주명 조회, 다음시작일 조회", walletAddress);
        }
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, checkFrDepositAccountNameResDto));
    }

    //자유입출금거래내역 조회
    @Operation(summary = "자유입출금거래내역조회", description = "/wallets/{walletAccountId}/transactions \n\n 자유입출금거래내역 조회")
    @GetMapping("/{walletAddress}/transactions")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getFrDepositAccountTransacions(@PathVariable String walletAddress,
                                                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                     Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<FrDepositTransactionListResDto> frDepositTransactionListResDtoList = frDepositService.getFrDepositTransactionList(walletAddress, userDetails.getId(), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, frDepositTransactionListResDtoList));
    }

    //잔액조회
    @Operation(summary = "내자유입출금잔액조회", description = "/wallets \n\n 나의자유입출금잔액조회")
    @GetMapping("")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getMyFrDepositBalance(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        FrDepositBalanceResDto frDepositBalanceResDto = frDepositService.getFrDepositBalance(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, frDepositBalanceResDto));
    }

    //계좌이체
    @Operation(summary = "계좌이체", description = "/wallets \n\n 자유입출금계좌이체")
    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> transferFromFrDeposit(@RequestBody @Validated FrDepositTransferReqDto frDepositTransferReqDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        FrDepositTransferResDto frDepositTransferResDto = FrDepositTransferResDto.builder()
                .remainingBalance(frDepositService.transferFrDeposit(frDepositTransferReqDto, userDetails.getId()))
                .build();
        log.info("계좌이체 userId: {}, 보내는주소 : {}",userDetails.getId(),frDepositTransferReqDto.getRecipientWalletAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, frDepositTransferResDto));
    }

    //자동이체예약
    @Operation(summary = "자동이체예약", description = "/wallets/autotransfer \n\n 자유입출금자동이체생성")
    @PostMapping("/autotransfer")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> createAutoTransfer(@RequestBody @Validated CreateAutomaticTransferReqDto createAutomaticTransferReqDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        frDepositService.createAutomaticTransfer(createAutomaticTransferReqDto, userDetails.getId());
        log.info("자동이체 생성 userId: {}, 보내는주소 : {}, 보내는 액수 : {}", userDetails.getId(), createAutomaticTransferReqDto.getRecipientWalletAddress(), createAutomaticTransferReqDto.getRecurringAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, "자동이체 등록되었습니다."));
    }

    //자동이체내역 조회
    @Operation(summary = "자동이체내역조회", description = "/wallets/autotransfer \n\n 자동이체내역조회")
    @GetMapping("/autotransfer")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getAutoTransferList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                     Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<GetAutomaticTransferLogResDto> getAutomaticTransferLogResDtoList = frDepositService.getAutomaticTransferList(userDetails.getId(), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, getAutomaticTransferLogResDtoList));
    }

    //자동이체상세정보 조회
    @Operation(summary = "자동이체상세정보", description = "/wallets/autotransfer/{autoTransferId}")
    @GetMapping("/autotransfer/{autoTransferId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getAutoTransferDetail(@PathVariable int autoTransferId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        GetAutomaticTransferDetailResDto automaticTransferDetail = frDepositService.getAutomaticTransferDetail(autoTransferId, userDetails.getId());
        log.info("userId: {}가 autoTransferTaskId: {} 상세조회", userDetails.getId(), autoTransferId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, automaticTransferDetail));
    }


    //자동이체 삭제
    @Operation(summary = "자동이체삭제", description = "/wallets/autotransfer/{autoTransferId}")
    @DeleteMapping("/autotransfer/{autoTransferId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> deleteAutoTransfer(@PathVariable int autoTransferId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        frDepositService.deleteAutomaticTransfer(autoTransferId, userDetails.getId());
        log.info("userId : {} autoTransferTask: {} 삭제 실행", userDetails.getId(), autoTransferId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "삭제완료"));
    }

    //자동이체 변경
    @Operation(summary = "자동이체변경", description = "/wallets/autotransfer/{autoTransferId}")
    @PatchMapping("/autotransfer/{autoTransferId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> updateAutoTransfer(@PathVariable int autoTransferId,@RequestBody @Validated PatchAutoTransferReqDto patchAutoTransferReqDto ,Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        frDepositService.updateAutomaticTransfer(patchAutoTransferReqDto,autoTransferId, userDetails.getId());
        log.info("userId : {} autoTransferTask: {} 변경 실행", userDetails.getId(), autoTransferId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "변경완료"));
    }

    //최근이체한 주소 리스트 조회
    @Operation(summary = "최근이체주소조회", description = "/wallets/list")
    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> getRecentTransferAddressList(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<RecentTransferAddressListResDto> recentTransferAddressList = frDepositService.getRecentTransferAddressList(userDetails.getId());
        log.info("userId : {} 최근 이체주소 내역 실행", userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, recentTransferAddressList));
    }


}

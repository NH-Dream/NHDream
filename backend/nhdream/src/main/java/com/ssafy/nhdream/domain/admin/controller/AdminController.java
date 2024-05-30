package com.ssafy.nhdream.domain.admin.controller;

import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.admin.dto.*;
import com.ssafy.nhdream.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
@Tag(name="7. ADMIN", description="ADMIN API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "교육 인증 심사", description = "관리자가 교육 인증 신청을 심사한다.")
    @PatchMapping("/training")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> reviewTraning(@RequestBody @Valid ReviewReqDto reviewReqDto) {

        log.info("신청자 PK : {}", reviewReqDto.getUserId());
        log.info("게시물 PK : {}", reviewReqDto.getApplyPostId());
        log.info("심사 결과 : {}", reviewReqDto.getResult());

        adminService.reviewTraning(reviewReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Scuess"));
    }

    @Operation(summary = "농부 인증 심사", description = "관리자가 농부 인증 신청을 심사한다.")
    @PatchMapping("/farmer")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> reviewFarmer(@RequestBody @Valid ReviewReqDto reviewReqDto) {

        log.info("신청자 PK : {}", reviewReqDto.getUserId());
        log.info("게시물 PK : {}", reviewReqDto.getApplyPostId());
        log.info("심사 결과 : {}", reviewReqDto.getResult());

        adminService.reviewFarmer(reviewReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Scuess"));
    }

    @Operation(summary = "대출 인증 심사", description = "관리자가 농부 인증 신청을 심사한다.")
    @PatchMapping("/loan")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> reviewLoan(@RequestBody @Valid LoanReviewReqDto loanReviewReqDto) {

        log.info("게시물 PK : {}", loanReviewReqDto.getApplyPostId());
        log.info("심사 결과 : {}", loanReviewReqDto.getResult());

        adminService.reviewLoan(loanReviewReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Scuess"));
    }

    @Operation(summary = "대출 심사 목록 조회", description = "대출 심사 목록 조회")
    @GetMapping("/review/loan")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 대출 심사 목록 반환")
    public ResponseEntity<? extends BaseResponseBody> getLoanReviewList(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
                                                                        @RequestParam(name = "status", required = false) String status,
                                                                        @RequestParam(name = "userName", required = false) String userName) {

        LoanReviewResDto loanReviewResDtos = adminService.getLoanReviewList(userName, status, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, loanReviewResDtos));
    }

    @Operation(summary = "대출 심사 상세 조회", description = "대출 심사 상세 조회")
    @GetMapping("/review/loan/{loanReviewId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 대출 심사 상세 정보 반환")
    public ResponseEntity<? extends BaseResponseBody> getLoanReviewDetail(@PathVariable(name = "loanReviewId") int loanReviewId) {

        LoanReviewDetailResDto loanReviewDetailResDto = adminService.getLoanReviewDetail(loanReviewId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, loanReviewDetailResDto));
    }

    @Operation(summary = "기간별 토큰 발급, 소각량 조회", description = "토큰 발급, 소각 내역 일별 조회")
    @GetMapping("/nhdc")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 일별 토근 발급, 소각 내역 반환")
    public ResponseEntity<? extends BaseResponseBody> getNHDCFlow(@RequestParam(value = "startDate") LocalDate startDate, @RequestParam(value = "endDate") LocalDate endDate) {

        List<NhTokenFlowDetailResDto> nhTokenFlowDetailResDtoList = adminService.getNhTokenFlowResList(startDate, endDate);
        log.info("관리자 기간별 토큰 발급, 소각량 조회 start: {}, end: {}",startDate,endDate);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, nhTokenFlowDetailResDtoList));
    }

    @Operation(summary = "기간별 토큰 거래량 조회", description = "토큰 거래 내역 일별 조회")
    @GetMapping("/nhdc/transaction")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 일별 토근 발급, 소각 내역 반환")
    public ResponseEntity<? extends BaseResponseBody> getNHDCTradeFlow(@RequestParam(value = "startDate") LocalDate startDate, @RequestParam(value = "endDate") LocalDate endDate) {

        List<NhTokenTradingVolumeDetailResDto> nhTokenFlowResDetailDtoList = adminService.getNhTokenTradeFlow(startDate, endDate);
        log.info("관리자 기간별 토큰 거래량 조회 start: {}, end: {}",startDate,endDate);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, nhTokenFlowResDetailDtoList));
    }

    @Operation(summary = "개인 지갑 발급량 조회", description = "개인 지갑 발급량 조회")
    @GetMapping("/nhdc/wallet-count")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 개인 지갑 발급량 반환")
    public ResponseEntity<? extends BaseResponseBody> getPersonalWalletCount() {
        log.info("관리자 개인지갑발급량 조회");
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, adminService.getPersonalWalletCount()));
    }

    @Operation(summary = "교육 심사 목록 조회", description = "교육 심사 목록 조회")
    @GetMapping("/review/training")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 대출 심사 목록 반환")
    public ResponseEntity<? extends BaseResponseBody> getTrainingReviewList(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
                                                                        @RequestParam(name = "status", required = false) String status,
                                                                        @RequestParam(name = "userName", required = false) String userName) {

        TrainingReviewResDto trainingReviewResDto = adminService.getTrainingReviewList(userName, status, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, trainingReviewResDto));
    }

    @Operation(summary = "교육 심사 목록 조회", description = "교육 심사 목록 조회")
    @GetMapping("/review/farmer")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 대출 심사 목록 반환")
    public ResponseEntity<? extends BaseResponseBody> getFarmerReviewList(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
                                                                            @RequestParam(name = "userName", required = false) String userName) {

        FarmerReviewResDto farmerReviewResDto = adminService.getFarmerReviewList(userName, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, farmerReviewResDto));
    }

    @Operation(summary = "농부 인증 상세 조회", description = "농부 인증 상세 조회")
    @GetMapping("/review/farmer/{farmerReviewId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 농부 인증 상세 정보 반환")
    public ResponseEntity<? extends BaseResponseBody> getFarmerReviewDetail(@PathVariable(name = "farmerReviewId") int farmerReviewId) {

        FarmerReviewDetailResDto farmerReviewDetailResDto = adminService.getFarmerReviewDetail(farmerReviewId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, farmerReviewDetailResDto));
    }

    @Operation(summary = "수료생 인증 상세 조회", description = "수료생 인증 상세 조회")
    @GetMapping("/review/training/{trainingReviewId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 수료생 인증 상세 정보 반환")
    public ResponseEntity<? extends BaseResponseBody> getTrainingReviewDetail(@PathVariable(name = "trainingReviewId") int trainingReviewId) {

        TrainingReviewDetailResDto trainingReviewDetailResDto = adminService.getTrainingReviewDetail(trainingReviewId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, trainingReviewDetailResDto));
    }

    @Operation(summary = "작업로그 리스트 조회", description = "작업로그 리스트 조회")
    @GetMapping("/tasklog")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 작업로그 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getLogList() {

        List<LogListResDto> logListResDtoList = adminService.getLogList();

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, logListResDtoList));
    }

    @Operation(summary = "작업로그 상세 조회", description = "작업로그 상세 조회")
    @GetMapping("/tasklog/{logId}")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 작업로그 리스트 반환")
    public ResponseEntity<? extends BaseResponseBody> getLogList(@PathVariable int logId) {

        LogDetailResDto logDetailResDto = adminService.getLogDetail(logId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, logDetailResDto));
    }
}

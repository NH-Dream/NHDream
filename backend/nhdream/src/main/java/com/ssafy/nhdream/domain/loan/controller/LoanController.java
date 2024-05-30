package com.ssafy.nhdream.domain.loan.controller;

import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.loan.dto.*;
import com.ssafy.nhdream.domain.loan.service.LoanService;
import com.ssafy.nhdream.domain.user.dto.TransactionListResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@Tag(name = "4. LOAN", description = "LOAN API")
public class LoanController {

    private final LoanService loanService;

    // 대출 목록 조회
    @GetMapping("/list")
    @Operation(summary = "대출 목록 조회", description = "가입할 수 있는 모든 대출의 목록을 조회한다.")
    public ResponseEntity<? extends BaseResponseBody> getLoanList() {
        log.info("getLoanList 실행");

        LoanListResDto loanListResDto = loanService.getLoanList();

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, loanListResDto));
    }

    // 대출 통장 상세 조회
    @GetMapping("/detail")
    @Operation(summary = "대출 통장 상세 조회", description = "대출 통장의 상세 정보를 조회한다.")
    public ResponseEntity<? extends BaseResponseBody> getLoanAccountDetail(
            @RequestParam(name = "id")
            @Parameter(description = "대출 통장 상세 정보를 조회할 대출 통장 id")
            int id) {
        log.info("{} : getLoanAccountDetail 실행", id);

        LoanResDto loanResDto = loanService.getLoanAccountDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, loanResDto));
    }

    // 대출 옵션 생성
    @PostMapping("/option")
    @Operation(summary = "대출 옵션 생성", description = "LoanReqDto를 받아 대출 옵션을 생성한다.")
    public ResponseEntity<? extends  BaseResponseBody> createLoanOption(
            @RequestBody
            LoanOptionReqDto loanOptionReqDto
    ) {
        log.info("createLoanOption 실행");

        int id = loanService.createLoanOption(loanOptionReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, id));
    }

    // 대출 옵션 조회
    @GetMapping("/option")
    @Operation(summary = "대출 옵션 상세 조회", description = "조회하고 싶은 대출 옵션의 id를 입력하면 상세 정보를 조회한다.")
    public ResponseEntity<? extends BaseResponseBody> getLoanOptionDetail (
            @RequestParam(name = "id")
            int id
    ) {
        log.info("{} getLoanOptionDetail 실행", id);

        LoanOptionDetailResDto loanOptionDetailResDto = loanService.getLoanOptionDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, loanOptionDetailResDto));
    }


    // 대출 심사 신청
    @PostMapping(value = "/approval", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "대출 심사 생성", description = "대출 심사에 대한 정보를 받아 대출 심사를 생성한다.")
    public ResponseEntity<? extends BaseResponseBody> createLoanApproval(
            @RequestParam(name = "userId")
            int userId,
            @RequestParam(name = "loanOptionId")
            int loanOptionId,
            @RequestParam(name = "files")
            List<MultipartFile> files
    ) {
        log.info("createLoanApproval 실행");

        int id = loanService.createLoanApproval(userId, loanOptionId, files);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, id));
    }

    // 내가 가입한 대출 목록 조회
    @GetMapping("/mylist")
    @Operation(summary = "내가 가입한 대출 목록 조회", description = "회원 id를 통해 내가 가입 신청을 넣은 대출 정보 리스트를 조회한다.")
    public ResponseEntity<? extends BaseResponseBody> getMyLoanList(
            @RequestParam(name = "id")
            int id
    ) {
        log.info("{} getMyLoanList 실행", id);

        MyLoanListResDto myLoanListResDto = loanService.getMyLoanList(id);


        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, myLoanListResDto));
    }

    // 내가 가입한 대출 상세 조회
    @GetMapping("/myloan")
    @Operation(summary = "내가 가입한 대출 상세 목록 조회", description = "대출 심사id를 통해 내가 가입 신청을 넣은 대출의 상세정보를 조회한다.")
    public ResponseEntity<? extends BaseResponseBody> getMyLoanDetail(
            @RequestParam(name = "id")
            int id
    ) {
        log.info("{} : getMyLoanDetail 실행", id);

        MyLoanDetailResDto myLoanDetailResDto = loanService.getMyLoanDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, myLoanDetailResDto));
    }


    // 대출 통장 생성
//    @PostMapping("/account")
//    @Operation(summary = "대출 통장 생성", description = "대출 통장에 대한 정보를 받아 대출 통장을 생성한다.")
//    public ResponseEntity<? extends BaseResponseBody> createLoanAccount() {
//        log.info("createLoanAccount 실행");
//
//        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, 0));
//    }


    // 대출 통장 이체


    // 대출 통장 거래 내역 조회


    // 대출 상환



    // 관리자에서 쓸 내용

    // 대출 상품 생성
    @PostMapping("/product")
    @Operation(summary = "대출 상품 생성", description = "관리자가 정보를 받아 대출 상품을 생성한다.")
    public ResponseEntity<? extends BaseResponseBody> createLoanProduct(
            @RequestBody
            LoanProductReqDto loanProductReqDto
    ) {
        log.info("createLoanProduct 실행");

        int id = loanService.createLoanProduct(loanProductReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, id));
    }

    // 대출 심사 수정
    @PatchMapping("/approval")
    @Operation(summary = "대출 심사 수정", description = "대출 심사를 완료 상태로 변경하고, 대출 통장을 생성한다.")
    public ResponseEntity<? extends BaseResponseBody> updateLoanApproval(
            @RequestParam(name = "id")
            int id
    ) {
        log.info("updateLoanApproval 실행");

        loanService.updateLoanApproval(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, 0));
    }

    // 대출 상품 수정
    @PatchMapping("/product")
    @Operation(summary = "대출 상품 수정", description = "대출 상품을 수정한다.")
    public ResponseEntity<? extends BaseResponseBody> updateLoanProduct(
            @RequestBody
            LoanProductReqDto loanProductReqDto
    ) {
        log.info("updateLoanProduct 실행");

        int id = loanService.updateLoanProduct(loanProductReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, id));
    }

    // 대출 상품 삭제
    @PatchMapping("/product/delete")
    @Operation(summary = "대출 상품 삭제", description = "대출 상품의 id와 삭제 시간을 입력받아 deleted_at을 추가해 삭제 상태로 변경한다.")
    public ResponseEntity<? extends BaseResponseBody> deleteLoanProduct(
            @RequestParam(name = "id")
            int id,
            @RequestParam(name = "deleted_at")
            LocalDate deletedAt
    ) {
        log.info("deleteLoanProduct 실행");

        loanService.deleteLoanProduct(id, deletedAt);

        return ResponseEntity.status((HttpStatus.OK)).body(BaseResponseBody.of(0, 0));
    }

    // 대출 상품 상세 조회
    @GetMapping("/product/{id}")
    @Operation(summary = "대출 상품 상세 조회", description = "조회하고 싶은 대출 상품의 id값을 받아 대출 상품을 조회한다.")
    public ResponseEntity<? extends BaseResponseBody> getLoanProductDetail(
            @PathVariable("id") int id
    ) {
        log.info("{} getLoanProductDetail 실행", id);

        LoanProductResDto loanProductResDto = loanService.getLoanProductDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, loanProductResDto));
    }


    @GetMapping("/transaction")
    public ResponseEntity<? extends BaseResponseBody> getTransactionList(
            @RequestParam(name = "id")
            int id
    ) {
        log.info("getTransactionList 실행");

        TransactionListResDto transactionListResDto = loanService.getTransactionList(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, transactionListResDto));
    }

}

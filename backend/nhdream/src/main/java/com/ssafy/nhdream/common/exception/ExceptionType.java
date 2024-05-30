package com.ssafy.nhdream.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {
    // 에러 열거
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부 오류입니다."),

    // 회원
    USER_NOT_EXIST(HttpStatus.UNAUTHORIZED, "U-001", "존재하지 않는 회원입니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.BAD_REQUEST, "U-002", "아이디가 중복되었습니다."),
    EMAIL_NOT_EXIST(HttpStatus.BAD_REQUEST, "U-003", "존재하지 않는 이메일입니다."),
    EMAIL_EXIST(HttpStatus.BAD_REQUEST, "U-004", "이미 사용 중인 이메일입니다."),
    CODE_TIME_EXPIRED(HttpStatus.BAD_REQUEST, "U-005", "인증 시간이 초과하였습니다."),
    CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "U-006", "인증 코드가 일치하지 않습니다."),
    DUPLICATE_LICENSE_NUM(HttpStatus.BAD_REQUEST, "U-007", "사업자 번호가 중복되었습니다."),
    FAILED_TO_CREATE_WALLET(HttpStatus.INTERNAL_SERVER_ERROR, "U-008", "지갑 생성에 실패하였습니다."),
    INVALID_BUSINESS_LICENSE(HttpStatus.BAD_REQUEST, "U-009", "유효하지 않은 사업자 등록 정보입니다."),
    WALLETPASSWORD_CONFIRMATION_MISMATCH(HttpStatus.BAD_REQUEST, "U-010", "새 비밀번호와 새 비밀번호 확인 값이 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "U-011", "비밀번호가 틀립니다."),
    PASSWORD_CONFIRMATION_MISMATCH(HttpStatus.BAD_REQUEST, "U-012", "새비밀번호와 확인 값이 일치하지 않습니다."),
    INVALID_USER_TYPE(HttpStatus.BAD_REQUEST, "U-013", "수료자만 신청할 수 있습니다."),

    //적금
    SAVINGPRODUCT_NOT_EXIST(HttpStatus.NOT_FOUND, "S-001", "존재하지 않는 적금상품입니다."),
    SAVINGOPTION_NOT_EXIST(HttpStatus.NOT_FOUND,"S-002", "존재하지 않는 적금옵션입니다."),
    EXCEED_MAX_MONTHLY_LIMIT(HttpStatus.BAD_REQUEST, "S-003", "가입납입액이 최대 월 납입한도보다 클 수 없습니다."),
    ALREADY_EXISTS_SAVING_ACCOUNT(HttpStatus.BAD_REQUEST, "S-004", "이미 해당 옵션의 적금 계좌가 있습니다."),
    SAVINGACCOUNT_NOT_EXIST(HttpStatus.NOT_FOUND,"S-005", "존재하지 않는 적금계좌입니다."),
    UNAUTHORIZED_SAVINGACCOUNT_ACCESS(HttpStatus.UNAUTHORIZED, "S-006", "적금계좌 접근 권한이 없습니다."),
    FAILED_TO_CREATE_SAVINGACCOUNT(HttpStatus.INTERNAL_SERVER_ERROR, "S-007", "적금계좌 블록체인 생성이 실패했습니다."),
    TERM_MISMATCH(HttpStatus.BAD_REQUEST, "S-008", "입력받은 기간과 옵션에 있는 기간이 다릅니다."),
    NOT_YET_EXPIRED(HttpStatus.BAD_REQUEST, "S-009", "아직 만기일이 되지 않았습니다."),
    AMOUNT_NOT_MATCH(HttpStatus.BAD_REQUEST, "S-010", "요번 거래의 납금액과 정해진 적금 월납금액이 다릅니다."),

    //토큰
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "T-001", "이체할 금액이 잔액보다 적습니다."),
    RECIPIENTACCOUNT_NOT_EXIST(HttpStatus.NOT_FOUND, "T-002", "보낼 주소가 존재하지 않습니다."),
    FAILED_TO_TRANSFER_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "T-003", "블록체인 토큰 전송이 실패했습니다."),
    SELF_TRANSFER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "T-004", "자기 자신에게 토큰 전송은 허용되지 않습니다."),
    AUTOTRANSFER_NOT_EXIST(HttpStatus.NOT_FOUND, "T-005", "해당 ID의 자동이체가 없습니다."),
    FAILTE_TO_FIRESTTOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "T-006", "초기 토큰 지급이 실패했습니다."),
    UNAUTHORIZED_AUTOTRANSFERINFO_ACCESS(HttpStatus.UNAUTHORIZED, "T-007", "자동이체상세 정보에 접근 권한이 없습니다."),


    //자유입출금(지갑)
    FRACCOUNT_NOT_EXIST(HttpStatus.NOT_FOUND, "F-001", "존재하지 않는 자유입출금계좌입니다."),
    UNAUTHORIZED_FRDEPOSITACCOUNT_ACCESS(HttpStatus.UNAUTHORIZED, "F-002", "자유입출금계좌 접근 권한이 없습니다."),
    WALLETPASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "F-003", "지갑비밀번호가 틀립니다."),


    // 대출
    LOANPRODUCT_NOT_EXIST(HttpStatus.NOT_FOUND, "L-001", "존재하지 않는 대출 상품입니다."),
    LOANACCOUNT_NOT_EXIST(HttpStatus.NOT_FOUND, "L-002", "존재하지 않는 대출 통장입니다."),
    LOANAPPROVAL_NOT_EXIST(HttpStatus.NOT_FOUND, "L-003", "존재하지 않는 대출 심사입니다."),
    LOANOPTIONS_NOT_EXIST(HttpStatus.NOT_FOUND, "L-004", "존재하지 않는 대출 옵션입니다."),
    UNAUTHORIZED_LOANACCOUNT_ACCESS(HttpStatus.UNAUTHORIZED, "L-005", "대출 통장에 접근할 권한이 없습니다."),
    NOT_FILES_SIZE4(HttpStatus.BAD_REQUEST, "L-006", "제출할 사진의 개수가 맞지 않습니다."),
    LOAN_REPAYMENT_ALREADY(HttpStatus.BAD_REQUEST, "L-007", "이미 상환된 대출입니다."),
    NOT_REMAINING_MONEY(HttpStatus.BAD_REQUEST, "L-008", "자유입출금 계좌에 잔액이 대출 상환액보다 적습니다."),
    FAILED_TO_CREATE_LOAN(HttpStatus.INTERNAL_SERVER_ERROR, "L-009", "블록체인 대출가입에 실패했습니다."),

    // 관리자
    TRAINING_NOT_EXIST(HttpStatus.BAD_REQUEST, "A-001", "존재하지 않는 심사 신청입니다."),
    CERTIFIED_NOT_EXIST(HttpStatus.BAD_REQUEST, "A-002", "존재하지 않는 농부 인증 신청입니다."),
    EDUCATION_NOT_EXIST(HttpStatus.BAD_REQUEST, "A-003", "존재하지 않는 수료 인증 신청입니다."),
    LOAN_APPLY_NOT_EXIST(HttpStatus.BAD_REQUEST, "A-004", "존재하지 않는 대출 신청입니다."),
    REVIEW_ALREADY_DONE(HttpStatus.BAD_REQUEST, "A-005", "이미 심사 완료한 신청입니다."),
    TASKLOG_NOT_EXIST(HttpStatus.BAD_REQUEST, "A-006", "존재하지 않는 작업 로그입니다."),
    
    // 바우처
    VOUCHER_NOT_EXIST(HttpStatus.NOT_FOUND, "V-001", "존재하지 않는 바우처입니다."),
    VOUCHER_ITEM_NOT_EXIST(HttpStatus.NOT_FOUND, "V-002", "존재하지 않는 바우처 상품입니다."),
    VOUCHER_ITEM_NOT_ENOUGH(HttpStatus.NOT_FOUND, "V-003", "상품 수량이 부족합니다."),
    VOUCHER_ITEM_NHDC_NOT_ENOUGH(HttpStatus.NOT_FOUND, "V-004", "결제에 필요한 NHDC 토큰이 부족합니다."),
    VOUCHER_ITEM_DRDC_NOT_ENOUGH(HttpStatus.NOT_FOUND, "V-005", "결제에 필요한 DRDC 토큰이 부족합니다."),
    VOUCHER_ITEM_BUY_ERROR(HttpStatus.NOT_FOUND, "V-006", "상품 결제에 문제가 생겼습니다."),

    // 정기예금
    REDEPOSIT_PRODUCT_NOT_EXIST(HttpStatus.NOT_FOUND, "R-001", "존재하지 않는 정기예금 상품입니다."),
    REDEPOSIT_OPTION_NOT_EXIST(HttpStatus.NOT_FOUND,"R-002", "존재하지 않는 예금옵션입니다."),
    REDEPOSIT_ACCOUNT_NOT_EXIST(HttpStatus.NOT_FOUND,"R-003", "존재하지 않는 예금계좌입니다."),
    ALREADY_EXISTS_REDEPOSIT_ACCOUNT(HttpStatus.BAD_REQUEST, "S-004", "이미 해당 옵션의 예금 계좌가 있습니다."),
    PRODUCT_OPTION_NOT_MATCH(HttpStatus.BAD_REQUEST, "R-005", "가입하고자하는 상품의 옵션이 아닙니다."),
    NOT_ENOUGH_MONEY(HttpStatus.BAD_REQUEST, "R-006", "충분한 돈이 있지 않습니다."),
    FAILED_TO_CREATE_ACCOUNT(HttpStatus.INTERNAL_SERVER_ERROR, "ㄲ-007", "예금계좌 블록체인 생성이 실패했습니다."),
    ;

    // 상태, 에러 코드, 메시지
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    // 생성자
    ExceptionType(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}

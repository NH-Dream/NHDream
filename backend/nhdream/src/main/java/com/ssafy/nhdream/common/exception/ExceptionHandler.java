package com.ssafy.nhdream.common.exception;


import com.ssafy.nhdream.common.response.BaseResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ResponseEntity<? extends BaseResponseBody> exceptions(CustomException e) {

        // 정의한 에러 코드 및 메시지 적용
        BaseResponseBody errorResponse = BaseResponseBody.error(e.getExceptionType().getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getExceptionType().getHttpStatus()).body(errorResponse);
    }

    // 유효성 예외 처리
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends BaseResponseBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.info("handleMethodArgumentNotValidException 입장");

        BindingResult bindingResult = e.getBindingResult();
        String errorCode = bindingResult.getFieldErrors().get(0).getCode();
        String errorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();

        // 정의한 에러 코드 및 메시지 적용
        BaseResponseBody errorResponse = BaseResponseBody.error(errorCode, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // HttpMessageNotReadableException 처리기 추가
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<? extends BaseResponseBody> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Error: ", e);
        String errorCode = "Invalid Data Format";
        String errorMessage = "입력 형식이 유효하지 않습니다. 숫자 형식을 확인해주세요.";
        BaseResponseBody errorResponse = BaseResponseBody.error(errorCode, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
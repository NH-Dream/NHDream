package com.ssafy.nhdream.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseBody<T> {

    @Getter
    @Setter
    public static class DataHeader {

        // successCode: 0 - 정상, 1 - 오류
        private int successCode;

        // resultCode: successCode가 오류(1) 인 경우 출력되는 코드값
        private String resultCode = "";

        // resultMessage: 결과 메시지
        private String resultMessage = "";
    }

    private DataHeader dataHeader = new DataHeader();
    private T dataBody;

    public static <T> BaseResponseBody<T> of(int successCode, T dataBody) {
        BaseResponseBody<T> body = new BaseResponseBody<>();
        body.getDataHeader().setSuccessCode(successCode);
        body.setDataBody(dataBody);
        return body;
    }

    


    public static BaseResponseBody<Object> error(String resultCode, String resultMessage) {
        BaseResponseBody<Object> body = new BaseResponseBody<>();
        body.getDataHeader().setSuccessCode(1);  // error
        body.getDataHeader().setResultCode(resultCode);
        body.getDataHeader().setResultMessage(resultMessage);
        return body;
    }
}

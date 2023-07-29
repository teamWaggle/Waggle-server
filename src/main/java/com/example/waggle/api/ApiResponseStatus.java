package com.example.waggle.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseStatus {
    SUCCESS(true, 1000, "요청에 성공하였습니다.", HttpStatus.OK);

    private final boolean isSuccess;
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ApiResponseStatus(boolean isSuccess, int code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

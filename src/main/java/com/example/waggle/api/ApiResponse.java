package com.example.waggle.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.example.waggle.api.ApiResponseStatus.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    private boolean isSuccess;
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ApiResponse(T data) {
        this.isSuccess = SUCCESS.isSuccess();
        this.httpStatus = HttpStatus.OK;
        this.message = SUCCESS.getMessage();
        this.data = data;
    }

    public ApiResponse(ApiResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.httpStatus = status.getHttpStatus();
        this.message = status.getMessage();
    }

}

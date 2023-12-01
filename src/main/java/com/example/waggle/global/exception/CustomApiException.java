package com.example.waggle.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomApiException extends RuntimeException{
    private final ErrorCode errorCode;
}

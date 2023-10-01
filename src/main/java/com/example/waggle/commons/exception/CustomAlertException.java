package com.example.waggle.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomAlertException extends RuntimeException{
    private final ErrorCode errorCode;
}

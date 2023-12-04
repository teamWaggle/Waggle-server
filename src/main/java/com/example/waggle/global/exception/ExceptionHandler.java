package com.example.waggle.global.exception;

import com.example.waggle.global.payload.code.BaseCode;

public class ExceptionHandler extends GeneralException{
    public ExceptionHandler(BaseCode code) {
        super(code);
    }
}

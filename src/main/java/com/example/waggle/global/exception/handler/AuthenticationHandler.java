package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class AuthenticationHandler extends GeneralException {
    public AuthenticationHandler(BaseCode code) {
        super(code);
    }
}

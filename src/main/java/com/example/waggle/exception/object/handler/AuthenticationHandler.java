package com.example.waggle.exception.object.handler;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.BaseCode;

public class AuthenticationHandler extends GeneralException {
    public AuthenticationHandler(BaseCode code) {
        super(code);
    }
}

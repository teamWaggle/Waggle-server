package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class SecurityHandler extends GeneralException {
    public SecurityHandler(BaseCode code) {
        super(code);
    }
}

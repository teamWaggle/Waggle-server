package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class SirenHandler extends GeneralException {
    public SirenHandler(BaseCode code) {
        super(code);
    }
}

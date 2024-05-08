package com.example.waggle.exception.object.handler;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.BaseCode;

public class SecurityHandler extends GeneralException {
    public SecurityHandler(BaseCode code) {
        super(code);
    }
}

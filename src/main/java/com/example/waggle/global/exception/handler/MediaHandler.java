package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class MediaHandler extends GeneralException {
    public MediaHandler(BaseCode code) {
        super(code);
    }
}

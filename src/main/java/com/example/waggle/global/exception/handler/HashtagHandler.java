package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class HashtagHandler extends GeneralException {
    public HashtagHandler(BaseCode code) {
        super(code);
    }
}

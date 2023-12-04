package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class ReplyHandler extends GeneralException {
    public ReplyHandler(BaseCode code) {
        super(code);
    }
}

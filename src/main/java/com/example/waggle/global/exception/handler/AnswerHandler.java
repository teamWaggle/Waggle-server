package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class AnswerHandler extends GeneralException {
    public AnswerHandler(BaseCode code) {
        super(code);
    }
}

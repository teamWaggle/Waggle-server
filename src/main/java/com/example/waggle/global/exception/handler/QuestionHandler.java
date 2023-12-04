package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class QuestionHandler extends GeneralException {
    public QuestionHandler(BaseCode code) {
        super(code);
    }
}

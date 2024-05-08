package com.example.waggle.exception.object.handler;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.BaseCode;

public class QuestionHandler extends GeneralException {
    public QuestionHandler(BaseCode code) {
        super(code);
    }
}

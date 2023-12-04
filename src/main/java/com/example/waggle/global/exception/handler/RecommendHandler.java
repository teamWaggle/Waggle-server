package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class RecommendHandler extends GeneralException {
    public RecommendHandler(BaseCode code) {
        super(code);
    }
}

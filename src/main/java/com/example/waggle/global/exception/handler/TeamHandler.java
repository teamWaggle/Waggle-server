package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class TeamHandler extends GeneralException {
    public TeamHandler(BaseCode code) {
        super(code);
    }
}

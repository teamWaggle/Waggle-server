package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class HelpHandler extends GeneralException {
    public HelpHandler(BaseCode code) {
        super(code);
    }
}

package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseCode code) {
        super(code);
    }
}

package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class FollowHandler extends GeneralException {
    public FollowHandler(BaseCode code) {
        super(code);
    }
}

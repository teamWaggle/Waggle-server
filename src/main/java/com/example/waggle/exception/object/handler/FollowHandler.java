package com.example.waggle.exception.object.handler;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.BaseCode;

public class FollowHandler extends GeneralException {
    public FollowHandler(BaseCode code) {
        super(code);
    }
}

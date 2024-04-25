package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class NotificationHandler extends GeneralException {
    public NotificationHandler(BaseCode code) {
        super(code);
    }
}

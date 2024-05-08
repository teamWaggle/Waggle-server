package com.example.waggle.exception.object.handler;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.BaseCode;

public class NotificationHandler extends GeneralException {
    public NotificationHandler(BaseCode code) {
        super(code);
    }
}

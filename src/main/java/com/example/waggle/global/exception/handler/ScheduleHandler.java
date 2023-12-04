package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class ScheduleHandler extends GeneralException {
    public ScheduleHandler(BaseCode code) {
        super(code);
    }
}

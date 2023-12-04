package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class StoryHandler extends GeneralException {
    public StoryHandler(BaseCode code) {
        super(code);
    }
}

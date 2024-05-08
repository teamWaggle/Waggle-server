package com.example.waggle.exception.object.handler;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.BaseCode;

public class StoryHandler extends GeneralException {
    public StoryHandler(BaseCode code) {
        super(code);
    }
}

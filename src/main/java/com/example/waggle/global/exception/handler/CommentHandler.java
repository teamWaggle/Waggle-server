package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class CommentHandler extends GeneralException {
    public CommentHandler(BaseCode code) {
        super(code);
    }
}

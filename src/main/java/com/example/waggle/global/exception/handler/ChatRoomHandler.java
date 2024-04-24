package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class ChatRoomHandler extends GeneralException {

    public ChatRoomHandler(BaseCode code) {
        super(code);
    }
}

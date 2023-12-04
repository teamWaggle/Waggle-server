package com.example.waggle.global.exception.handler;

import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.BaseCode;

public class PetHandler extends GeneralException {
    public PetHandler(BaseCode code) {
        super(code);
    }
}

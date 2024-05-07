package com.example.waggle.exception.object.handler;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.BaseCode;

public class PetHandler extends GeneralException {
    public PetHandler(BaseCode code) {
        super(code);
    }
}

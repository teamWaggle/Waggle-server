package com.example.waggle.component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorCode {
    MEMBER_NOT_FOUND(200, "9999", "Member Not Found Error Exception");

    private int status;
    private String divisionCode;
    private String message;

    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}

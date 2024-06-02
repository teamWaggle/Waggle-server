package com.example.waggle.global.annotation.api;

import com.example.waggle.exception.payload.code.ErrorStatus;

import java.util.List;

public enum PredefinedErrorStatus {
    DEFAULT(List.of(ErrorStatus._INTERNAL_SERVER_ERROR)),
    AUTH(List.of(
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER
    ));

    private final List<ErrorStatus> errorStatuses;

    PredefinedErrorStatus(List<ErrorStatus> errorStatuses) {
        this.errorStatuses = errorStatuses;
    }

    public List<ErrorStatus> getErrorStatuses() {
        return errorStatuses;
    }
}

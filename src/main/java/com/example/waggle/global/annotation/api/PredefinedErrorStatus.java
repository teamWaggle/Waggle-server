package com.example.waggle.global.annotation.api;

import com.example.waggle.exception.payload.code.ErrorStatus;

import java.util.List;

public enum PredefinedErrorStatus {
    DEFAULT(List.of(ErrorStatus._INTERNAL_SERVER_ERROR)),
    AUTH(List.of(
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND
    )),
    BOARD_DATA_CHANGE(List.of(
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.BOARD_CANNOT_EDIT_OTHERS,
            ErrorStatus.BOARD_NOT_FOUND,
            ErrorStatus.MEMBER_NOT_FOUND
    )),
    ADMIN(List.of(
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.AUTH_ROLE_CANNOT_EXECUTE_URI,
            ErrorStatus.MEMBER_NOT_FOUND
    ));

    private final List<ErrorStatus> errorStatuses;

    PredefinedErrorStatus(List<ErrorStatus> errorStatuses) {
        this.errorStatuses = errorStatuses;
    }

    public List<ErrorStatus> getErrorStatuses() {
        return errorStatuses;
    }
}

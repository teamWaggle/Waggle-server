package com.example.waggle.global.payload.code;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    // 서버 오류
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),

    // 일반적인 요청 오류
    _BAD_REQUEST(BAD_REQUEST, 4000, "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, 4001, "로그인이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, 4002, "금지된 요청입니다."),


    // 인증 관련 오류 (4050 ~ 4099)
    AUTH_INVALID_REFRESH_TOKEN(BAD_REQUEST, 4050, "유효하지 않은 리프레시 토큰입니다."),
    AUTH_MISMATCH_REFRESH_TOKEN(BAD_REQUEST, 4051, "리프레시 토큰과 사용자 정보가 일치하지 않습니다."),
    AUTH_INVALID_TOKEN(UNAUTHORIZED, 4052, "유효하지 않은 인증 토큰입니다."),
    AUTH_UNAUTHORIZED_MEMBER(UNAUTHORIZED, 4053, "계정 정보를 찾을 수 없습니다."),
    AUTH_TOKEN_NO_AUTHORITY(UNAUTHORIZED, 4054, "권한 정보가 없는 토큰입니다."),


    // 회원 관련 오류 (4100 ~ 4149)
    MEMBER_DUPLICATE_USERNAME(CONFLICT, 4100, "이미 존재하는 사용자 이름입니다."),
    MEMBER_INFO_REQUIRED_REGISTER(BAD_REQUEST, 4101, "회원가입 시 필요한 정보를 모두 입력해주세요."),
    MEMBER_NOT_FOUND(NOT_FOUND, 4102, "해당 사용자 정보를 찾을 수 없습니다."),
    MEMBER_REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, 4103, "로그아웃 된 사용자입니다."),
    MEMBER_SIGN_IN_INFO_REQUIRED(BAD_REQUEST, 4104, "아이디나 비밀번호를 확인해주세요."),



    // 게시판 관련 오류 (4150 ~ 4199)
    BOARD_CANNOT_RECOMMEND_OWN(BAD_REQUEST, 4150, "자신의 게시물에는 좋아요를 누를 수 없습니다."),
    BOARD_CANNOT_EDIT_OTHERS(BAD_REQUEST, 4151, "다른 사용자의 게시물을 수정하거나 삭제할 수 없습니다."),
    BOARD_INVALID_TYPE(BAD_REQUEST, 4152,"잘못된 게시글 유형입니다."),
    BOARD_NOT_FOUND(NOT_FOUND, 4153, "존재하지 않는 게시글입니다."),
    RECOMMEND_NOT_FOUND(NOT_FOUND,4154,"존재하지 않는 추천입니다"),
    COMMENT_NOT_FOUND(NOT_FOUND, 4155,"존재하지 않는 댓글입니다"),
    COMMENT_CANNOT_EDIT_OTHERS(BAD_REQUEST, 4156, "다른 사용자의 댓글을 수정하거나 삭제할 수 없습니다."),
    REPLY_NOT_FOUND(NOT_FOUND, 4157,"존재하지 않는 대댓글입니다"),
    REPLY_CANNOT_EDIT_OTHERS(BAD_REQUEST, 4158, "다른 사용자의 대댓글을 수정하거나 삭제할 수 없습니다."),


    // 스케줄 관련 오류 (4200 ~ 4249)
    TEAM_NOT_FOUND(NOT_FOUND, 4200,"팀 정보가 존재하지 않습니다"),
    SCHEDULE_NOT_FOUND(NOT_FOUND,4201,"스케줄 정보가 존재하지 않습니다"),
    TEAM_MEMBER_ALREADY_EXISTS(CONFLICT, 4202, "이미 팀에 속해 있는 멤버입니다"),
    TEAM_LEADER_UNAUTHORIZED(BAD_REQUEST, 4203, "팀 리더만 리더를 변경할 수 있습니다"),
    TEAM_MEMBER_NOT_IN_TEAM(NOT_FOUND, 4204, "멤버가 이 팀에 속해 있지 않습니다"),




    // 펫 관련 오류 (4250 ~ 4299)
    PET_NOT_FOUND(NOT_FOUND, 4250,"펫 정보가 존재하지 않습니다");


    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    @Override
    public Reason getReason() {
        return Reason.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public Reason getReasonHttpStatus() {
        return Reason.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
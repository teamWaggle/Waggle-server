package com.example.waggle.global.payload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(BAD_REQUEST,4000,"잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED,4001,"로그인이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, 4003, "금지된 요청입니다."),

    // 에러 추가
    TEST_EXCEPTION(BAD_REQUEST, 4001, "Test Error Message"),

    //errorCode 붙여넣기
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(BAD_REQUEST,4000, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, 4000,"리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    CANNOT_RECOMMEND_MYSELF(BAD_REQUEST, 4000,"자신의 게시물에 좋아요를 누를 수 없습니다"),
    CANNOT_TOUCH_NOT_YOURS(BAD_REQUEST, 4000,"타인의 게시물 혹은 정보를 수정 및 삭제할 수 없습니다"),
    INVALID_BOARD_TYPE(BAD_REQUEST, 4000,"게시글의 타입이 맞지 않습니다"),
    ALREADY_USING_USERNAME(BAD_REQUEST,4000,"이미 사용중인 사용자 이름입니다"),
    MUST_WRITE_INFO_SIGN_IN(BAD_REQUEST,4000,"아이디나 비밀번호를 확인해주세요"),
    MUST_WRITE_INFO_SIGN_UP(BAD_REQUEST,4000,"회원 가입 시 정보를 작성해주세요"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, 4001,"권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, 4001,"현재 내 계정 정보가 존재하지 않습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, 4004, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, 4004,"로그아웃 된 사용자입니다"),
    BOARD_NOT_FOUND(NOT_FOUND,4004,"존재하지 않는 게시글입니다"),
    RECOMMEND_NOT_FOUND(NOT_FOUND,4004,"존재하지 않는 추천입니다"),
    COMMENT_NOT_FOUND(NOT_FOUND, 4004,"존재하지 않는 댓글입니다"),
    REPLY_NOT_FOUND(NOT_FOUND, 4004,"존재하지 않는 대댓글입니다"),
    PET_NOT_FOUND(NOT_FOUND, 4004,"펫 정보가 존재하지 않습니다"),
    TEAM_NOT_FOUND(NOT_FOUND, 4004,"팀 정보가 존재하지 않습니다"),
    SCHEDULE_NOT_FOUND(NOT_FOUND,4004,"스케줄 정보가 존재하지 않습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, 4009, "데이터가 이미 존재합니다"),
    DUPLICATE_USERNAME(CONFLICT,4009,"존재하는 userName 입니다");


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
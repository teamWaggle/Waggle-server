package com.example.waggle.global.payload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    // 서버 오류
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),
    _UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR(INTERNAL_SERVER_ERROR, 5001, "서버 에러, 로그인이 필요없는 요청입니다."),
    _ASSIGNABLE_PRINCIPAL(BAD_REQUEST, 5002, "인증타입이 잘못되어 할당이 불가능합니다."),

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
    AUTH_REDIRECT_NOT_MATCHING(BAD_REQUEST, 4055, "redirect uri가 서버 내 설정과 매칭되지 않습니다."),
    AUTH_ROLE_CANNOT_EXECUTE_URI(BAD_REQUEST, 4056, "사용자는 해당 로직을 수행할 수 없는 역할군입니다."),
    AUTH_MUST_AUTHORIZED_URI(UNAUTHORIZED, 4057, "해당 uri는 권한 인증이 필수입니다. 만료된 토큰이거나 인증 정보가 없습니다."),
    AUTH_REFRESH_NOT_EXIST_IN_COOKIE(UNAUTHORIZED, 4058, "cookie에 refresh token이 존재하지 않습니다"),

    // 회원 관련 오류 (4100 ~ 4149)
    MEMBER_DUPLICATE_USERNAME(CONFLICT, 4100, "이미 존재하는 사용자 이름입니다."),
    MEMBER_INFO_REQUIRED_REGISTER(BAD_REQUEST, 4101, "회원가입 시 필요한 정보를 모두 입력해주세요."),
    MEMBER_NOT_FOUND(NOT_FOUND, 4102, "해당 사용자 정보를 찾을 수 없습니다."),
    MEMBER_REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, 4103, "로그아웃 된 사용자입니다."),
    MEMBER_SIGN_IN_INFO_REQUIRED(BAD_REQUEST, 4104, "아이디나 비밀번호를 확인해주세요."),
    MEMBER_DUPLICATE_EMAIL(CONFLICT, 4105, "이미 사용중인 이메일입니다."),
    MEMBER_DUPLICATE_NICKNAME(CONFLICT, 4106, "이미 사용중인 닉네임입니다."),


    // 게시판 관련 오류 (4150 ~ 4199)
    BOARD_CANNOT_RECOMMEND_OWN(BAD_REQUEST, 4150, "자신의 게시물에는 좋아요를 누를 수 없습니다."),
    BOARD_CANNOT_EDIT_OTHERS(BAD_REQUEST, 4151, "다른 사용자의 게시물을 수정하거나 삭제할 수 없습니다."),
    BOARD_INVALID_TYPE(BAD_REQUEST, 4152, "잘못된 게시글 유형입니다."),
    BOARD_NOT_FOUND(NOT_FOUND, 4153, "존재하지 않는 게시글입니다."),
    RECOMMEND_NOT_FOUND(NOT_FOUND, 4154, "존재하지 않는 추천입니다"),
    COMMENT_NOT_FOUND(NOT_FOUND, 4155, "존재하지 않는 댓글입니다"),
    COMMENT_CANNOT_EDIT_OTHERS(BAD_REQUEST, 4156, "다른 사용자의 댓글을 수정하거나 삭제할 수 없습니다."),
    REPLY_NOT_FOUND(NOT_FOUND, 4157, "존재하지 않는 대댓글입니다."),
    REPLY_CANNOT_EDIT_OTHERS(BAD_REQUEST, 4158, "다른 사용자의 대댓글을 수정하거나 삭제할 수 없습니다."),


    // 스케줄 관련 오류 (4200 ~ 4249)
    TEAM_NOT_FOUND(NOT_FOUND, 4200, "팀 정보가 존재하지 않습니다."),
    SCHEDULE_NOT_FOUND(NOT_FOUND, 4201, "스케줄 정보가 존재하지 않습니다."),
    TEAM_MEMBER_ALREADY_EXISTS(CONFLICT, 4202, "이미 팀에 속해 있는 멤버입니다."),
    TEAM_LEADER_UNAUTHORIZED(BAD_REQUEST, 4203, "팀 리더만 리더를 변경할 수 있습니다."),
    TEAM_MEMBER_NOT_IN_TEAM(NOT_FOUND, 4204, "멤버가 이 팀에 속해 있지 않습니다."),
    TEAM_PARTICIPATION_REQUEST_ALREADY_EXISTS(CONFLICT, 4205, "이미 팀 참여 요청이 존재합니다."),
    TEAM_PARTICIPATION_NOT_FOUND(NOT_FOUND, 4206, "참여 요청 정보가 존재하지 않습니다."),
    TEAM_LEADER_ONLY_CAN_DELETE_TEAM(BAD_REQUEST, 4207, "팀 리더만 팀을 지울 수 있습니다."),
    TEAM_MEMBER_CANNOT_BE_EXCEEDED(BAD_REQUEST, 4208, "팀 멤버 수 제한을 초과할 수 없습니다."),
    TEAM_LEADER_CANNOT_BE_REMOVED(BAD_REQUEST, 4209, "팀 리더는 팀 멤버에서 제외될 수 없습니다."),
    SCHEDULE_NOT_IN_YOUR_TEAM_SCHEDULE(BAD_REQUEST, 4210, "해당 스케줄은 당신이 소속한 팀의 스케줄이 아닙니다"),
    SCHEDULE_START_TIME_IS_LATER_THAN_END_TIME(BAD_REQUEST, 4211, "시작시간이 종료시간보다 늦습니다."),
    TEAM_SIZE_IS_OVER_THAN_REQUEST_SIZE(BAD_REQUEST, 4212, "현재 존재하는 팀멤버의 사이즈가 업데이트하고자 하는 팀멤버 사이즈보다 큽니다"),
    SCHEDULE_WRITER_CANNOT_DELETE_MEMBER_SCHEDULE(BAD_REQUEST, 4213, "작성자는 멤버 스케줄에서 제외가 불가능합니다"),
    SCHEDULE_CANNOT_COMMENTED_BECAUSE_OF_ACCESS(BAD_REQUEST, 4214, "개인 스케줄이 아니므로 댓글을 달 수 없습니다."),


    // 펫 관련 오류 (4250 ~ 4299)
    PET_NOT_FOUND(NOT_FOUND, 4250, "펫 정보가 존재하지 않습니다"),
    PET_INFO_CANNOT_EDIT_OTHERS(BAD_REQUEST, 4251, "펫 정보는 펫 주인 당사자만 변경 가능합니다"),

    // 팔로우 관련 오류 (4300 ~ 4349)
    FOLLOW_NOT_FOUND(NOT_FOUND, 4300, "해당 팔로우가 목록에 존재하지 않습니다"),
    FOLLOW_NOT_AUTHENTICATED_UNFOLLOW(BAD_REQUEST, 4301, "팔로우를 신청한 사람이 아닙니다. 따라서 팔로우 취소가 불가능합니다"),
    FOLLOW_ALREADY_EXIST(BAD_REQUEST, 4302, "이미 존재하는 팔로우입니다."),
    FOLLOW_NOT_ALLOWED_MYSELF(BAD_REQUEST, 4303, "자기 자신은 팔로우를 못합니다."),

    // 미디어 관련 오류(4350 ~ 4399)
    MEDIA_NOT_FOUND(NOT_FOUND, 4350, "존재하지 않는 미디어입니다."),
    MEDIA_REQUEST_IS_EMPTY(BAD_REQUEST, 4351, "request 미디어 파일이 존재하지 않습니다."),
    MEDIA_REQUEST_STILL_EXIST(BAD_REQUEST, 4352, "request 할 미디어 파일이 남아있습니다"),
    MEDIA_COUNT_IS_DIFFERENT(BAD_REQUEST, 4353, "새로 업로드 요청한 파일의 개수와 실제 post한 파일의 개수가 일치하지 않습니다"),
    MEDIA_PREFIX_IS_WRONG(BAD_REQUEST, 4354, "요청한 이미지 url의 prefix가 잘못되었습니다.");


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
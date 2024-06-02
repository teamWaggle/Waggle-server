package com.example.waggle.exception.payload.code;

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
    _ASSIGNABLE_PARAMETER(BAD_REQUEST, 5002, "인증타입이 잘못되어 할당이 불가능합니다."),

    // 일반적인 요청 오류
    _BAD_REQUEST(BAD_REQUEST, 4000, "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, 4001, "로그인이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, 4002, "금지된 요청입니다."),


    // 인증 관련 오류 (4050 ~ 4099)
    AUTH_INVALID_REFRESH_TOKEN(BAD_REQUEST, 4050, "유효하지 않은 리프레시 토큰입니다."),
    AUTH_MISMATCH_REFRESH_TOKEN(BAD_REQUEST, 4051, "리프레시 토큰과 사용자 정보가 일치하지 않습니다."),
    AUTH_INVALID_TOKEN(UNAUTHORIZED, 4052, "유효하지 않은 인증 토큰입니다."),
    AUTH_UNAUTHORIZED_MEMBER(UNAUTHORIZED, 4053, "계정 정보를 찾을 수 없습니다."),
    AUTH_TOKEN_HAS_EXPIRED(UNAUTHORIZED, 4054, "토큰의 유효기간이 만료되었습니다."),
    AUTH_REDIRECT_NOT_MATCHING(BAD_REQUEST, 4055, "redirect uri가 서버 내 설정과 매칭되지 않습니다."),
    AUTH_ROLE_CANNOT_EXECUTE_URI(BAD_REQUEST, 4056, "사용자는 해당 로직을 수행할 수 없는 역할군입니다."),
    AUTH_MUST_AUTHORIZED_URI(UNAUTHORIZED, 4057, "해당 uri는 권한 인증이 필수입니다. 만료된 토큰이거나 인증 정보가 없습니다."),
    AUTH_REFRESH_NOT_EXIST_IN_COOKIE(UNAUTHORIZED, 4058, "cookie에 refresh token이 존재하지 않습니다"),
    AUTH_MISMATCH_EMAIL_AND_PASSWORD(UNAUTHORIZED, 4059, "이메일과 패스워드가 일치하는 회원정보가 존재하지 않습니다"),
    AUTH_OAUTH2_EMAIL_NOT_FOUND_FROM_PROVIDER(UNAUTHORIZED, 4060, "provider로부터 받아올 email의 정보가 존재하지 않습티다"),
    AUTH_TOKEN_IS_UNSUPPORTED(UNAUTHORIZED, 4061, "토큰 형식이 jwt와는 다른 형식입니다."),
    AUTH_IS_NULL(BAD_REQUEST, 4062, "토큰 정보가 null입니다."),
    AUTH_PROVIDER_IS_NOT_MATCH(BAD_REQUEST, 4063, "로그인 하려는 소셜과 회원이 회원가입한 provider가 일치하지 않습니다"),

    // 회원 관련 오류 (4100 ~ 4149)
    MEMBER_DUPLICATE_USER_URL(CONFLICT, 4100, "이미 존재하는 사용자 url 입니다."),
    MEMBER_INFO_REQUIRED_REGISTER(BAD_REQUEST, 4101, "회원가입 시 필요한 정보를 모두 입력해주세요."),
    MEMBER_NOT_FOUND(NOT_FOUND, 4102, "해당 사용자 정보를 찾을 수 없습니다."),
    MEMBER_REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, 4103, "로그아웃 된 사용자입니다."),
    MEMBER_SIGN_IN_INFO_REQUIRED(BAD_REQUEST, 4104, "아이디나 비밀번호를 확인해주세요."),
    MEMBER_DUPLICATE_EMAIL(CONFLICT, 4105, "이미 사용중인 이메일입니다."),
    MEMBER_DUPLICATE_NICKNAME(CONFLICT, 4106, "이미 사용중인 닉네임입니다."),
    MEMBER_NAME_TYPE_IS_INVALID(NOT_ACCEPTABLE, 4107, "랜덤 입력할 필드의 타입을 잘못 입력하였습니다"),
    MEMBER_EMAIL_VERIFICATION_FAILED(BAD_REQUEST, 4108, "인증번호가 일치하지 않습니다. 이메일 인증에 실패했습니다."),
    MEMBER_ACCESS_DENIED_BY_AUTHORIZATION(BAD_REQUEST, 4109, "인가가 잘못된 요청입니다."),


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
    BOARD_OBJECT_CANNOT_BE_NULL_WHEN_CHECK_RECOMMEND(BAD_REQUEST, 4159, "좋아요를 확인할때 게시글 객체가 널이어서는 안됩니다"),
    BOARD_TYPE_CANNOT_BE_FOUND_WHEN_CHECK_RECOMMEND(BAD_REQUEST, 4160, "좋아요를 확인할 때 검증하는 게시글 타입이 아닙니다"),
    RECOMMEND_WAS_NOT_INITIATED(BAD_REQUEST, 4161, "redis에 해당 멤버의 recommend정보가 초기화되지 않았습니다."),
    RECOMMEND_WAS_ALREADY_INITIATED(BAD_REQUEST, 4162, "redis에 해당 멤버의 recommend정보를 이미 초기화했습니다."),
    BOARD_SEARCHING_KEYWORD_IS_TOO_SHORT(BAD_REQUEST, 4163, "검색어는 최소 2자입니다."),


    // 스케줄 관련 오류 (4200 ~ 4249)
    TEAM_NOT_FOUND(NOT_FOUND, 4200, "팀 정보가 존재하지 않습니다."),
    SCHEDULE_NOT_FOUND(NOT_FOUND, 4201, "스케줄 정보가 존재하지 않습니다."),
    TEAM_MEMBER_ALREADY_EXISTS(CONFLICT, 4202, "이미 팀에 속해 있는 멤버입니다."),
    TEAM_LEADER_UNAUTHORIZED(BAD_REQUEST, 4203, "팀 리더 권한 행동입니다. 사용자는 해당 팀의 리더가 아닙니다."),
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
    TEAM_SIZE_IS_OVER_THAN_MAX_SIZE(BAD_REQUEST, 4215, "수용할 수 있는 팀멤버를 초과하는 요청입니다"),
    SCHEDULE_WAS_ALREADY_CHOSEN(BAD_REQUEST, 4216, " 이미 선택하신 스케줄입니다."),
    PARTICIPATION_NOT_FOUND(NOT_FOUND, 4217, "존재하지 않는 참가 지원입니다."),
    TEAM_MEMBER_IS_OVER_THAN_ONE(BAD_REQUEST, 4218, "팀 멤버 수가 1명보다 많습니다. 팀 삭제는 한명이 남았을 때만 가능합니다."),


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
    MEDIA_PREFIX_IS_WRONG(BAD_REQUEST, 4354, "요청한 이미지 url의 prefix가 잘못되었습니다."),

    // 채팅 관련 오류(4400 ~ 4449)
    CHAT_ROOM_NOT_FOUND(NOT_FOUND, 4400, "존재하지 않는 채팅방입니다."),
    CHAT_ROOM_MEMBER_NOT_FOUND(NOT_FOUND, 4401, "회원이 채팅방에 존재하지 않습니다."),
    CHAT_ROOM_ACCESS_DENIED(FORBIDDEN, 4402, "채팅방 접근이 거부되었습니다."),
    CHAT_ROOM_LEAVE_DENIED(FORBIDDEN, 4403, "채팅방에서의 퇴장이 거부되었습니다."),
    CHAT_MESSAGE_NOT_FOUND(NOT_FOUND, 4404, "존재하지 않는 메세지입니다."),
    CHAT_ROOM_MEMBER_ALREADY_EXISTS(CONFLICT, 4405, "이미 채팅방의 멤버입니다."),

    // 알림 관련 오류(4450 ~ 4499)
    NOTIFICATION_NOT_FOUND(NOT_FOUND, 4450, "존재하지 않는 알림입니다"),
    NOTIFICATION_NOT_YOURS(BAD_REQUEST, 4451, "당신의 알림이 아닙니다");


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

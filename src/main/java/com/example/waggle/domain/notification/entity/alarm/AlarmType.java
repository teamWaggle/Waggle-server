package com.example.waggle.domain.notification.entity.alarm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {

    PARTICIPATION_MY_TEAM("우리팀에 참여요청이 있었어요!"),
    PARTICIPATION_IS_REJECTED("팀 참여에 거부되었습니다"),
    PARTICIPATION_IS_ACCEPTED("팀 참여 승락이 되었습니다!"),
    MENTION_IN_COMMENT("누군가 댓글에 사용자를 언급했습니다!"),
    FOLLOW_NOTIFICATION("누군가가 당신을 팔로우했습니다!");

    private final String alarmContent;
}

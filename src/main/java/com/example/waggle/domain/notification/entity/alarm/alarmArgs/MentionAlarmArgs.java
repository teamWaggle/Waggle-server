package com.example.waggle.domain.notification.entity.alarm.alarmArgs;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MentionAlarmArgs extends AlarmArgs {
    private Long boardId;
    private Long commentId;
}

package com.example.waggle.domain.notification.entity.alarm.alarmArgs;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ParticipationAlarmArgs extends AlarmArgs {
    private Long teamId;
}

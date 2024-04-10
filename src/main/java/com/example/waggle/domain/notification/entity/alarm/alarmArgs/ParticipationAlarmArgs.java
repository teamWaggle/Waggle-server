package com.example.waggle.domain.notification.entity.alarm.alarmArgs;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
public class ParticipationAlarmArgs extends AlarmArgs {
    private Long teamId;
}

package com.example.waggle.domain.notification.entity.alarm.alarmArgs;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
public class MentionAlarmArgs extends AlarmArgs {
    private Long boardId;
    private Long commentId;
}

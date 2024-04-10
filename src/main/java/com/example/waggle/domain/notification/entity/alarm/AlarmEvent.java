package com.example.waggle.domain.notification.entity.alarm;

import com.example.waggle.domain.notification.entity.sse.SseEventName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AlarmEvent {
    private AlarmType type;
    private AlarmArgs args;
    private Long memberId;
    private SseEventName eventName;
}

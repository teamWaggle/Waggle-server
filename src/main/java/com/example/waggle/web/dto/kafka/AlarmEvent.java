package com.example.waggle.web.dto.kafka;

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
    private Long userId;
    private SseEventName eventName;
}

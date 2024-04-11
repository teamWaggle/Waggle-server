package com.example.waggle.domain.notification.entity.alarm.alarmArgs;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
public class AlarmArgs implements Serializable {

    private static final long serialVersionUID = 300L;
    private String callingMemberUserUrl;
    private String linkUrl;
}

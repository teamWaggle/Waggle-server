package com.example.waggle.web.converter;

import com.example.waggle.domain.notification.entity.alarm.Alarm;
import com.example.waggle.web.dto.alarm.AlarmResponse.AlarmListDto;
import com.example.waggle.web.dto.alarm.AlarmResponse.AlarmViewDto;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmConverter {

    public static AlarmViewDto toAlarmViewDto(Alarm alarm) {
        return AlarmViewDto.builder()
                .alarmId(alarm.getId())
                .alarmArgs(alarm.getAlarmArgs())
                .text(alarm.getAlarmType().getAlarmContent())
                .build();
    }

    public static AlarmListDto toAlarmListDto(List<Alarm> alarmList) {
        return AlarmListDto.builder()
                .alarmViewDtoList(alarmList.stream()
                        .map(AlarmConverter::toAlarmViewDto)
                        .collect(Collectors.toList())
                )
                .build();
    }
}

package com.example.waggle.global.util;

import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.ScheduleStatus;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;

import java.time.LocalDateTime;

import static com.example.waggle.domain.schedule.entity.ScheduleStatus.*;

public class ScheduleUtil {

    public static ScheduleStatus setStatus(Schedule schedule) {
        if (LocalDateTime.now().isBefore(schedule.getStartTime())) {
            return UPCOMING;
        } else if (LocalDateTime.now().isAfter(schedule.getEndTime())) {
            return CLOSING;
        } else if (LocalDateTime.now().isAfter(schedule.getStartTime()) && LocalDateTime.now().isBefore(schedule.getEndTime())) {
            return IN_PROGRESS;
        } else {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_START_TIME_IS_LATER_THAN_END_TIME);
        }
    }

    public static void validateSchedule(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_START_TIME_IS_LATER_THAN_END_TIME);
        }
    }
}

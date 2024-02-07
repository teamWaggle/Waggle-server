package com.example.waggle.global.util;

import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;

import java.time.LocalDateTime;

import static com.example.waggle.global.util.ScheduleUtil.Status.*;

public class ScheduleUtil {
    public enum Status {
        IN_PROGRESS, UPCOMING, CLOSING
    }

    public static Status setStatus(Schedule schedule) {
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
}

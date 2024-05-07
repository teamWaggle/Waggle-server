package com.example.waggle.global.util;

import com.example.waggle.domain.schedule.persistence.entity.Schedule;
import com.example.waggle.domain.schedule.persistence.entity.ScheduleStatus;
import com.example.waggle.exception.object.handler.ScheduleHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.waggle.domain.schedule.persistence.entity.ScheduleStatus.*;

public class ScheduleUtil {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static ScheduleStatus setStatus(Schedule schedule) {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isBefore(schedule.getStartDate())) {
            return UPCOMING;
        } else if (currentDate.isAfter(schedule.getEndDate())) {
            return CLOSING;
        } else if (!currentDate.isBefore(schedule.getStartDate()) && !currentDate.isAfter(schedule.getEndDate())) {
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

    public static void validateSchedule(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_START_TIME_IS_LATER_THAN_END_TIME);
        }
    }

    public static void validateSchedule(LocalTime start, LocalTime end) {
        if (start.isAfter(end)) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_START_TIME_IS_LATER_THAN_END_TIME);
        }
    }

    public static LocalTime convertLocalTime(String time) {
        return LocalTime.parse(time, TIME_FORMATTER);
    }

}

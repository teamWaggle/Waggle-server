package com.example.waggle.domain.schedule.presentation.converter;

import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.schedule.persistence.entity.Schedule;
import com.example.waggle.global.util.ScheduleUtil;
import com.example.waggle.domain.schedule.presentation.dto.schedule.ScheduleResponse;
import com.example.waggle.domain.schedule.presentation.dto.schedule.ScheduleResponse.OverlappedScheduleDto;
import com.example.waggle.domain.schedule.presentation.dto.schedule.ScheduleResponse.ScheduleDetailDto;
import com.example.waggle.domain.schedule.presentation.dto.schedule.ScheduleResponse.ScheduleListDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleConverter {

    public static ScheduleDetailDto toScheduleDetailDto(Schedule schedule) {
        return ScheduleDetailDto.builder()
                .boardId(schedule.getId())
                .teamId(schedule.getTeam().getId())
                .teamColor(schedule.getTeam().getTeamColor())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(LocalDateTime.of(schedule.getStartDate(), schedule.getStartTime()))
                .endDate(LocalDateTime.of(schedule.getEndDate(), schedule.getEndTime()))
                .createdDate(schedule.getCreatedDate())
                .status(ScheduleUtil.setStatus(schedule))
                .scheduleOwner(MemberConverter.toMemberSummaryDto(schedule.getMember()))
                .build();
    }

    public static ScheduleListDto toScheduleListDto(Page<Schedule> pagedSchedules) {
        List<ScheduleDetailDto> schedules = pagedSchedules.stream()
                .map(ScheduleConverter::toScheduleDetailDto).collect(Collectors.toList());
        boolean isPrivate = pagedSchedules.getContent().stream()
                .map(schedule -> schedule.getTeam().getIsPrivate())
                .allMatch(Boolean::booleanValue);

        return ScheduleListDto.builder()
                .scheduleList(schedules)
                .scheduleCount(pagedSchedules.getTotalElements())
                .isFirst(pagedSchedules.isFirst())
                .isLast(pagedSchedules.isLast())
                .isPrivate(isPrivate)
                .build();
    }

    public static ScheduleListDto toScheduleListDto(List<Schedule> schedules) {
        return ScheduleListDto.builder()
                .scheduleList(
                        schedules.stream().map(ScheduleConverter::toScheduleDetailDto).collect(Collectors.toList()))
                .scheduleCount(schedules.size())
                .build();
    }

    public static OverlappedScheduleDto toOverlappedScheduleDto(Schedule schedule) {
        return OverlappedScheduleDto.builder()
                .scheduleTitle(schedule.getTitle())
                .teamName(schedule.getTeam().getName())
                .teamColor(schedule.getTeam().getTeamColor())
                .build();
    }

    public static ScheduleResponse.OverlappedScheduleListDto toOverlappedScheduleListDto(List<Schedule> scheduleList) {
        return ScheduleResponse.OverlappedScheduleListDto.builder()
                .overlappedScheduleDtoList(
                        scheduleList.stream()
                                .map(ScheduleConverter::toOverlappedScheduleDto).collect(Collectors.toList())
                )
                .scheduleCount(scheduleList.size())
                .build();
    }

    public static void setIsScheduledInList(ScheduleListDto scheduleListDto, HashMap<Long, Boolean> isScheduledMap) {
        scheduleListDto.getScheduleList().forEach(scheduleDto ->
                scheduleDto.setIsScheduled(isScheduledMap.get(scheduleDto.getBoardId()))
        );
    }

    public static void setOverlappedScheduleCount(ScheduleListDto scheduleListDto, HashMap<Long, Long> countMap) {
        scheduleListDto.getScheduleList().forEach(scheduleDto ->
                scheduleDto.setOverlappedScheduleCount(Math.toIntExact(countMap.get(scheduleDto.getBoardId())))
        );
    }

}

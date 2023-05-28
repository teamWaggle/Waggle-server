package com.example.waggle.dto.member;

import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.domain.team.Team;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ScheduleDto {
    private Long id;
    private Long teamId;
    private String title;
    private String description;
    private LocalDateTime scheduleTime;
    @Builder.Default
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    static public ScheduleDto toDto(Schedule schedule) {
        return ScheduleDto.builder()
                .id(schedule.getId())
                .teamId(schedule.getTeam().getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .scheduleTime(schedule.getScheduleTime())
                .scheduleMembers(schedule.getScheduleMembers())
                .build();
    }

    public Schedule toEntity(Team team) {
        return Schedule.builder()
                .id(id)
                .team(team)
                .title(title)
                .description(description)
                .scheduleTime(scheduleTime)
                .scheduleMembers(scheduleMembers)
                .build();
    }

}

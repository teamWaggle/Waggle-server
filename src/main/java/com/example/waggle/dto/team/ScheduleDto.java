package com.example.waggle.dto.team;

import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.domain.team.Team;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
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
    private List<String> scheduleMembers = new ArrayList<>();

    static public ScheduleDto toDto(Schedule schedule) {
        return ScheduleDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .scheduleTime(schedule.getScheduleTime())
                .scheduleMembers(schedule.getScheduleMembers().stream()
                        .map(sm -> sm.getMember().getUsername()).collect(Collectors.toList()))
                .build();
    }

    public Schedule toEntity(Team team, List<ScheduleMember> scheduleMembers) {
        return Schedule.builder()
                .team(team)
                .title(title)
                .description(description)
                .scheduleTime(scheduleTime)
                .scheduleMembers(scheduleMembers).build();
    }

}

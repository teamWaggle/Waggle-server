package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.member.entity.ScheduleMember;
import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.domain.Team;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
//                .description(schedule.getDescription())
//                .scheduleTime(schedule.getScheduleTime())
//                .scheduleMembers(schedule.getScheduleMembers().stream()
//                        .map(sm -> sm.getMember().getUsername()).collect(Collectors.toList()))
                .build();
    }

    public Schedule toEntity(Team team, List<ScheduleMember> scheduleMembers) {
        return Schedule.builder()
                .team(team)
                .title(title)
//                .description(description)
//                .scheduleTime(scheduleTime)
//                .scheduleMembers(scheduleMembers)
                .build();
    }

}

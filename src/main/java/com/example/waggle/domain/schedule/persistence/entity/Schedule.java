package com.example.waggle.domain.schedule.persistence.entity;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.global.util.ScheduleUtil;
import com.example.waggle.domain.schedule.presentation.dto.schedule.ScheduleRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule extends Board {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    public void update(ScheduleRequest updateScheduleRequest) {
        this.title = updateScheduleRequest.getTitle();
        this.content = updateScheduleRequest.getContent();
        this.startDate = updateScheduleRequest.getStartDate();
        this.endDate = updateScheduleRequest.getEndDate();
        this.startTime = ScheduleUtil.convertLocalTime(updateScheduleRequest.getStartTime());
        this.endTime = ScheduleUtil.convertLocalTime(updateScheduleRequest.getEndTime());
    }

}

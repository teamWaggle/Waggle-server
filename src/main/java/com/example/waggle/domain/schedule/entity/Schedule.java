package com.example.waggle.domain.schedule.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.web.dto.schedule.ScheduleRequest.ScheduleCreateDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public void update(ScheduleCreateDto request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
    }

}

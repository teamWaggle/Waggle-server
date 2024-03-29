package com.example.waggle.domain.schedule.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

    public void update(ScheduleRequest updateScheduleRequest) {
        this.title = updateScheduleRequest.getTitle();
        this.content = updateScheduleRequest.getContent();
        this.startTime = updateScheduleRequest.getStartTime();
        this.endTime = updateScheduleRequest.getEndTime();
    }

}

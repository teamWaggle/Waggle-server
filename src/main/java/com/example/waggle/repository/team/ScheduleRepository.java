package com.example.waggle.repository.team;

import com.example.waggle.domain.team.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByTeamId(Long teamId);
}

package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.domain.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByTeamId(Long teamId);

}

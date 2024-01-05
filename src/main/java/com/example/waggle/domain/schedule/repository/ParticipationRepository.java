package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByTeamIdAndUsername(Long teamId, String username);

    Optional<Participation> findByTeamIdAndUsername(Long teamId, String username);

}

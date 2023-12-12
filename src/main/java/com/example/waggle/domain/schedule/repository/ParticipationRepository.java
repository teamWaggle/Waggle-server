package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.domain.Participation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByTeamIdAndUsername(Long teamId, String username);

    Optional<Participation> findByTeamIdAndUsername(Long teamId, String username);

}

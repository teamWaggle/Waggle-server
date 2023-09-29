package com.example.waggle.board.sos.repository;

import com.example.waggle.board.sos.domain.Sos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SosRepository extends JpaRepository<Sos, Long> {

    List<Sos> findByMemberUsername(@Param("username") String username);
}

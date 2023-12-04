package com.example.waggle.domain.board.help.repository;

import com.example.waggle.domain.board.help.entity.Help;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpRepository extends JpaRepository<Help, Long> {

    Page<Help> findByMemberUsername(String username, Pageable pageable);

    List<Help> findAll();

    Page<Help> findAll(Pageable pageable);
}

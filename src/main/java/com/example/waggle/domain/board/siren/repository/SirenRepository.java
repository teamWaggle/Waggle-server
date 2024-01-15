package com.example.waggle.domain.board.siren.repository;

import com.example.waggle.domain.board.siren.entity.Siren;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SirenRepository extends JpaRepository<Siren, Long> {

    Page<Siren> findByMemberUsername(String username, Pageable pageable);

    List<Siren> findAll();

    Page<Siren> findAll(Pageable pageable);
}

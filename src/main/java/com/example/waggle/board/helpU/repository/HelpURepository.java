package com.example.waggle.board.helpU.repository;

import com.example.waggle.board.helpU.domain.HelpU;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HelpURepository extends JpaRepository<HelpU, Long> {

    Page<HelpU> findByMemberUsername(String username,Pageable pageable);

    List<HelpU> findAll();

    Page<HelpU> findAll(Pageable pageable);
}

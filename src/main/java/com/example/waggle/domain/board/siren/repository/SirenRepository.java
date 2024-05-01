package com.example.waggle.domain.board.siren.repository;

import com.example.waggle.domain.board.siren.entity.Siren;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SirenRepository extends JpaRepository<Siren, Long> {

    Page<Siren> findByMemberUsername(String username, Pageable pageable);

    Page<Siren> findByMemberId(Long memberId, Pageable pageable);

    List<Siren> findListByMemberUsername(String username);

    List<Siren> findAll();

    Page<Siren> findAll(Pageable pageable);

    void deleteAllByMemberUsername(String username);

    List<Siren> findAllByOrderByStatusAsc();

    @Query("SELECT s.viewCount FROM Siren s WHERE s.id = :boardId")
    Long findViewCountByBoardId(@Param("boardId") Long boardId);

    @Transactional
    @Modifying
    @Query("update Siren s set s.viewCount = :viewCount where s.id = :boardId")
    void applyViewCntToRDB(@Param("boardId") Long boardId, @Param("viewCount") Long viewCount);

}

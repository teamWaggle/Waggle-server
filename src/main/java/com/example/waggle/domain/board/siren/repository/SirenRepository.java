package com.example.waggle.domain.board.siren.repository;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SirenRepository extends JpaRepository<Siren, Long>, SirenQueryRepository {

    Page<Siren> findByMemberUsername(String username, Pageable pageable);

    Page<Siren> findByMemberUserUrl(String userUrl, Pageable pageable);

    Page<Siren> findByMemberId(Long memberId, Pageable pageable);

    Page<Siren> findByCategory(SirenCategory category, Pageable pageable);

    List<Siren> findListByMemberUsername(String username);

    List<Siren> findAll();

    Page<Siren> findAll(Pageable pageable);

    void deleteAllByMemberUsername(String username);

    List<Siren> findAllByOrderByStatusAsc();

}

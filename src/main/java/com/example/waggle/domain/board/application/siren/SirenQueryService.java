package com.example.waggle.domain.board.application.siren;

import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SirenQueryService {
    List<Siren> getAllSiren();

    Page<Siren> getPagedSirenList(Pageable pageable);

    List<Siren> getRepresentativeSirenList();

    Page<Siren> getPagedSirenListByUsername(String username, Pageable pageable);

    Page<Siren> getPagedSirenListByUserUrl(String userUrl, Pageable pageable);

    Page<Siren> getPagedSirenListByMemberId(Long memberId, Pageable pageable);

    Page<Siren> getPagedSirenListByFilter(SirenFilterParam filterParam, Pageable pageable);

    Page<Siren> getPagedSirenListByCategory(SirenCategory category, Pageable pageable);

    Siren getSirenByBoardId(Long boardId);

    List<Siren> getRandomUnsolvedSirenList();

}
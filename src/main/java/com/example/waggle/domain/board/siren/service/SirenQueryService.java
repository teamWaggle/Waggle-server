package com.example.waggle.domain.board.siren.service;

import com.example.waggle.domain.board.siren.entity.Siren;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SirenQueryService {
    List<Siren> getAllSiren();

    Page<Siren> getPagedSirenList(Pageable pageable);

    List<Siren> getRepresentativeSirenList();

    Page<Siren> getPagedSirenListByUsername(String username, Pageable pageable);

    Page<Siren> getPagedSirenListByMemberId(Long memberId, Pageable pageable);

    Siren getSirenByBoardId(Long boardId);
}
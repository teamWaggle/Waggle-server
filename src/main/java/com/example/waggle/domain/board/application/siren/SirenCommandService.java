package com.example.waggle.domain.board.application.siren;

import com.example.waggle.domain.board.presentation.dto.siren.SirenRequest;
import com.example.waggle.domain.member.persistence.entity.Member;


public interface SirenCommandService {

    Long createSiren(SirenRequest createSirenRequest,
                     Member member);

    Long updateSiren(Long boardId,
                     SirenRequest updateSirenRequest,
                     Member member);

    void convertStatus(Long boardId, Member member);

    void deleteSiren(Long boardId, Member member);

    void deleteSirenWithRelations(Long boardId, Member member);

}
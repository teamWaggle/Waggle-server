package com.example.waggle.domain.board.siren.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.siren.SirenRequest;


public interface SirenCommandService {

    Long createSiren(SirenRequest createSirenRequest,
                     Member member);

    Long updateSiren(Long boardId,
                     SirenRequest updateSirenRequest,
                     Member member);

    void convertStatus(Long boardId, Member member);

    void deleteSiren(Long boardId, Member member);

}
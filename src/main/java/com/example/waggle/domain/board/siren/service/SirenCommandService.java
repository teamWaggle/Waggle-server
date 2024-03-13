package com.example.waggle.domain.board.siren.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.siren.SirenRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SirenCommandService {

    Long createSiren(SirenRequest createSirenRequest,
                     List<MultipartFile> multipartFiles,
                     Member member);

    Long updateSiren(Long boardId,
                     SirenRequest updateSirenRequest,
                     MediaUpdateDto updateMediaRequest,
                     List<MultipartFile> multipartFiles,
                     Member member);

    void deleteSiren(Long boardId, Member member);

    void increaseSirenViewCount(Long boardId);

}
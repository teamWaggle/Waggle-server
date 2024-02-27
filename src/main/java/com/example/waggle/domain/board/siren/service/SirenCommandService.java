package com.example.waggle.domain.board.siren.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.siren.SirenRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SirenCommandService {
    Long createSiren(SirenRequest createSirenRequest,
                     List<MultipartFile> multipartFiles);

    Long createSirenByUsername(SirenRequest createSirenRequest,
                               List<MultipartFile> multipartFiles,
                               String username);

    Long updateSiren(Long boardId,
                     SirenRequest updateSirenRequest,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles);

    Long updateSirenV2(Long boardId,
                       SirenRequest updateSirenRequest,
                       MediaUpdateDto updateMediaRequest,
                       List<MultipartFile> multipartFiles);

    Long updateSirenByUsername(Long boardId,
                               String username,
                               SirenRequest updateSirenRequest,
                               MediaUpdateDto updateMediaRequest,
                               List<MultipartFile> multipartFiles);

    void deleteSiren(Long boardId);

    void deleteSirenByUsername(Long boardId, String username);
}
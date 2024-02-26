package com.example.waggle.domain.board.siren.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.siren.SirenRequest.SirenCreateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SirenCommandService {
    Long createSiren(SirenCreateDto sirenWriteDto,
                     List<MultipartFile> multipartFiles);

    Long createSirenByUsername(SirenCreateDto sirenWriteDto,
                               List<MultipartFile> multipartFiles,
                               String username);

    Long updateSiren(Long boardId,
                     SirenCreateDto request,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles);

    Long updateSirenV2(Long boardId,
                       SirenCreateDto request,
                       MediaUpdateDto mediaUpdateDto,
                       List<MultipartFile> multipartFiles);

    Long updateSirenByUsername(Long boardId,
                               String username,
                               SirenCreateDto request,
                               MediaUpdateDto mediaUpdateDto,
                               List<MultipartFile> multipartFiles);

    void deleteSiren(Long boardId);

    void deleteSirenByUsername(Long boardId, String username);
}
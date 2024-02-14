package com.example.waggle.domain.board.siren.service;

import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.siren.SirenRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SirenCommandService {
    Long createSiren(SirenRequest.Post sirenWriteDto,
                     List<MultipartFile> multipartFiles);

    Long createSirenByUsername(SirenRequest.Post sirenWriteDto,
                               List<MultipartFile> multipartFiles,
                               String username);

    Long updateSiren(Long boardId,
                     SirenRequest.Post request,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles);

    Long updateSirenV2(Long boardId,
                       SirenRequest.Post request,
                       MediaRequest.Put mediaUpdateDto,
                       List<MultipartFile> multipartFiles);

    void deleteSiren(Long boardId);
}
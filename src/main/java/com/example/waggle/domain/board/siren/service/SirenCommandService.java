package com.example.waggle.domain.board.siren.service;

import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.siren.SirenRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SirenCommandService {
    Long createSiren(SirenRequest.Post sirenWriteDto,
                     List<MultipartFile> multipartFiles) throws IOException;

    Long createSirenByUsername(SirenRequest.Post sirenWriteDto,
                               List<MultipartFile> multipartFiles,
                               String username) throws IOException;

    Long updateSiren(Long boardId,
                     SirenRequest.Put sirenUpdateDto,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles) throws IOException;

    Long updateSirenV2(Long boardId,
                       SirenRequest.Put sirenUpdateDto,
                       MediaRequest.Put mediaUpdateDto,
                       List<MultipartFile> multipartFiles) throws IOException;

    void deleteSiren(Long boardId);
}
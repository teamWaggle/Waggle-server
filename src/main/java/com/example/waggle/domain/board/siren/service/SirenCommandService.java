package com.example.waggle.domain.board.siren.service;

import com.example.waggle.web.dto.siren.SirenRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SirenCommandService {
    Long createSiren(SirenRequest.Post helpUWriteDto,
                     List<MultipartFile> multipartFiles) throws IOException;

    Long updateSiren(Long boardId,
                    SirenRequest.Put helpUWriteDto,
                    List<MultipartFile> multipartFiles,
                    List<String> deleteFiles) throws IOException;

    Long updateSirenV2(Long boardId,
                      SirenRequest.Put helpUpdateDto,
                      MediaRequest.Put mediaUpdateDto,
                      List<MultipartFile> multipartFiles) throws IOException;

    void deleteSiren(Long boardId);
}
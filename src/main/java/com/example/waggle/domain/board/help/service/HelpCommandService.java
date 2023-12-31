package com.example.waggle.domain.board.help.service;

import com.example.waggle.web.dto.help.HelpRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HelpCommandService {
    Long createHelp(HelpRequest.Post helpUWriteDto,
                    List<MultipartFile> multipartFiles) throws IOException;

    Long updateHelp(Long boardId,
                    HelpRequest.Put helpUWriteDto,
                    List<MultipartFile> multipartFiles,
                    List<String> deleteFiles)throws IOException;

    Long updateHelpV2(Long boardId,
                      HelpRequest.Put helpUpdateDto,
                      MediaRequest.Put mediaUpdateDto,
                      List<MultipartFile> multipartFiles) throws IOException;

    void deleteHelp(Long boardId);
}
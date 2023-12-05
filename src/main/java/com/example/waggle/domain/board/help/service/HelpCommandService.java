package com.example.waggle.domain.board.help.service;

import com.example.waggle.web.dto.help.HelpRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HelpCommandService {
    Long createHelp(HelpRequest.Post helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    Long createHelpTest(HelpRequest.Post helpUWriteDto, String username);


    Long updateHelp(Long boardId, HelpRequest.Post helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail)throws IOException;

    void deleteHelp(Long boardId);
}
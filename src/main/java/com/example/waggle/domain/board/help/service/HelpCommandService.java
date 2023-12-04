package com.example.waggle.domain.board.help.service;

import com.example.waggle.web.dto.help.HelpWriteDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HelpCommandService {
    Long createHelp(HelpWriteDto helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    Long createHelpTest(HelpWriteDto helpUWriteDto, String username);


    Long updateHelp(Long boardId, HelpWriteDto helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail)throws IOException;

    void deleteHelp(Long boardId);
}
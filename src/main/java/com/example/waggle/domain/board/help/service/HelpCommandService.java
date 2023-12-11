package com.example.waggle.domain.board.help.service;

import com.example.waggle.web.dto.help.HelpRequest;

import java.io.IOException;

public interface HelpCommandService {
    Long createHelp(HelpRequest.Post helpUWriteDto) throws IOException;

    Long updateHelp(Long boardId, HelpRequest.Post helpUWriteDto)throws IOException;

    void deleteHelp(Long boardId);
}
package com.example.waggle.domain.board.help.service;

import com.example.waggle.domain.board.help.entity.Help;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HelpQueryService {
    List<Help> getAllHelp();

    Page<Help> getPagedHelpList(Pageable pageable);

    Page<Help> getPagedHelpListByUsername(String username, Pageable pageable);

    Help getHelpByBoardId(Long boardId);
}
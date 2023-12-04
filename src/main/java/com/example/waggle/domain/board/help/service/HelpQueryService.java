package com.example.waggle.domain.board.help.service;

import com.example.waggle.web.dto.help.HelpDetailDto;
import com.example.waggle.web.dto.help.HelpSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HelpQueryService {
    List<HelpSummaryDto> getAllHelp();

    Page<HelpSummaryDto> getPagedHelpList(Pageable pageable);

    Page<HelpSummaryDto> getPagedHelpListByUsername(String username, Pageable pageable);

    HelpDetailDto getHelpByBoardId(Long boardId);
}
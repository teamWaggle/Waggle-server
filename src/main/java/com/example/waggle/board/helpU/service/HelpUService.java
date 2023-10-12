package com.example.waggle.board.helpU.service;

import com.example.waggle.board.helpU.dto.HelpUDetailDto;
import com.example.waggle.board.helpU.dto.HelpUSummaryDto;
import com.example.waggle.board.helpU.dto.HelpUWriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HelpUService {

    List<HelpUSummaryDto> getAllHelpU();

    Page<HelpUSummaryDto> getPagedHelpUs(Pageable pageable);

    Page<HelpUSummaryDto> getPagedHelpUsByUsername(String username, Pageable pageable);

    HelpUDetailDto getHelpUByBoardId(Long boardId);

    Long createHelpU(HelpUWriteDto helpUWriteDto);

    Long createHelpUTest(HelpUWriteDto helpUWriteDto, String username);

    Long createHelpUWithThumbnail(HelpUWriteDto helpUWriteDto, String thumbnail);

    Long updateHelpU(Long boardId, HelpUWriteDto helpUWriteDto);

    void deleteHelpU(Long boardId);



}

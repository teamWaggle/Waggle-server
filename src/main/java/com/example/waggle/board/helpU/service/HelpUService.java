package com.example.waggle.board.helpU.service;

import com.example.waggle.board.helpU.dto.HelpUDetailDto;
import com.example.waggle.board.helpU.dto.HelpUSummaryDto;
import com.example.waggle.board.helpU.dto.HelpUWriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HelpUService {

    List<HelpUSummaryDto> getAllHelpU();

    Page<HelpUSummaryDto> getAllHelpUByPaging(Pageable pageable);

    Page<HelpUSummaryDto> getHelpUsByUsername(String username, Pageable pageable);

    HelpUDetailDto getHelpUById(Long helpUId);

    Long createHelpU(HelpUWriteDto helpUWriteDto);

    Long updateHelpU(HelpUWriteDto helpUWriteDto);

    void deleteHelpU(HelpUDetailDto helpUDetailDto);



}

package com.example.waggle.domain.board.helpU.service;

import com.example.waggle.web.dto.helpU.HelpUDetailDto;
import com.example.waggle.web.dto.helpU.HelpUSummaryDto;
import com.example.waggle.web.dto.helpU.HelpUWriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HelpUService {

    List<HelpUSummaryDto> getAllHelpU();

    Page<HelpUSummaryDto> getPagedHelpUs(Pageable pageable);

    Page<HelpUSummaryDto> getPagedHelpUsByUsername(String username, Pageable pageable);

    HelpUDetailDto getHelpUByBoardId(Long boardId);

    Long createHelpU(HelpUWriteDto helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    Long createHelpUTest(HelpUWriteDto helpUWriteDto, String username);


    Long updateHelpU(Long boardId, HelpUWriteDto helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail)throws IOException;

    void deleteHelpU(Long boardId);



}

package com.example.waggle.domain.board.help.service;

import com.example.waggle.web.dto.help.HelpDetailDto;
import com.example.waggle.web.dto.help.HelpSummaryDto;
import com.example.waggle.web.dto.help.HelpWriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HelpService {

    List<HelpSummaryDto> getAllHelp();

    Page<HelpSummaryDto> getPagedHelpList(Pageable pageable);

    Page<HelpSummaryDto> getPagedHelpListByUsername(String username, Pageable pageable);

    HelpDetailDto getHelpByBoardId(Long boardId);

    Long createHelp(HelpWriteDto helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    Long createHelpTest(HelpWriteDto helpUWriteDto, String username);


    Long updateHelp(Long boardId, HelpWriteDto helpUWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail)throws IOException;

    void deleteHelp(Long boardId);



}

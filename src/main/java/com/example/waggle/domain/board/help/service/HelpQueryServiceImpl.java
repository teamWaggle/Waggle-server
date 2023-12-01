package com.example.waggle.domain.board.help.service;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.global.exception.CustomApiException;
import com.example.waggle.web.dto.help.HelpDetailDto;
import com.example.waggle.web.dto.help.HelpSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.global.exception.ErrorCode.BOARD_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HelpQueryServiceImpl implements HelpQueryService{

    private final HelpRepository helpRepository;

    @Override
    public List<HelpSummaryDto> getAllHelp() {
        List<Help> all = helpRepository.findAll();
        log.info("all size is ={}",all.size());
        return all.stream().map(HelpSummaryDto::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<HelpSummaryDto> getPagedHelpList(Pageable pageable) {
        Page<Help> all = helpRepository.findAll(pageable);
        return all.map(HelpSummaryDto::toDto);
    }

    @Override
    public Page<HelpSummaryDto> getPagedHelpListByUsername(String username, Pageable pageable) {
        Page<Help> pagehelpByUsername = helpRepository.findByMemberUsername(username, pageable);
        return pagehelpByUsername.map(HelpSummaryDto::toDto);
    }

    @Override
    public HelpDetailDto getHelpByBoardId(Long boardId) {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));

        return HelpDetailDto.toDto(help);
    }
}

package com.example.waggle.domain.board.help.service;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.global.exception.handler.HelpHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HelpQueryServiceImpl implements HelpQueryService {

    private final HelpRepository helpRepository;

    @Override
    public List<Help> getAllHelp() {
        List<Help> all = helpRepository.findAll();
        return all;
    }

    @Override
    public Page<Help> getPagedHelpList(Pageable pageable) {
        Page<Help> all = helpRepository.findAll(pageable);
        return all;
    }

    @Override
    public Page<Help> getPagedHelpListByUsername(String username, Pageable pageable) {
        Page<Help> pageHelpByUsername = helpRepository.findByMemberUsername(username, pageable);
        return pageHelpByUsername;
    }

    @Override
    public Help getHelpByBoardId(Long boardId) {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        return help;
    }
}
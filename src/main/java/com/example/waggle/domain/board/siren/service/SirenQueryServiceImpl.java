package com.example.waggle.domain.board.siren.service;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
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
public class SirenQueryServiceImpl implements SirenQueryService {

    private final SirenRepository sirenRepository;

    @Override
    public List<Siren> getAllSiren() {
        List<Siren> all = sirenRepository.findAll();
        return all;
    }

    @Override
    public Page<Siren> getPagedSirenList(Pageable pageable) {
        Page<Siren> all = sirenRepository.findAll(pageable);
        return all;
    }

    @Override
    public Page<Siren> getPagedSirenListByUsername(String username, Pageable pageable) {
        Page<Siren> pageHelpByUsername = sirenRepository.findByMemberUsername(username, pageable);
        return pageHelpByUsername;
    }

    @Override
    public Siren getSirenByBoardId(Long boardId) {
        Siren help = sirenRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        return help;
    }
}
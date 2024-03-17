package com.example.waggle.domain.board.siren.service;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.SirenHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SirenQueryServiceImpl implements SirenQueryService {

    private final SirenRepository sirenRepository;
    private final RecommendRepository recommendRepository;

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
    public List<Siren> getRepresentativeSirenList() {
        List<Siren> sirens = sirenRepository.findAll();
        Map<Long, Long> recommendCountBySirenId = recommendRepository.findRecommendsForSirens().stream()
                .collect(Collectors.groupingBy(recommend -> recommend.getBoard().getId(), Collectors.counting()));

        return sirens.stream()
                .sorted(Comparator.comparing((Siren siren) -> siren.getStatus() != ResolutionStatus.UNRESOLVED)
                        .thenComparing(siren -> recommendCountBySirenId.getOrDefault(siren.getId(), 0L),
                                Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Siren> getPagedSirenListByUsername(String username, Pageable pageable) {
        Page<Siren> pageSirenByUsername = sirenRepository.findByMemberUsername(username, pageable);
        return pageSirenByUsername;
    }

    @Override
    public Page<Siren> getPagedSirenListByMemberId(Long memberId, Pageable pageable) {
        return sirenRepository.findByMemberId(memberId, pageable);
    }

    @Override
    public Siren getSirenByBoardId(Long boardId) {
        return sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));
    }
}
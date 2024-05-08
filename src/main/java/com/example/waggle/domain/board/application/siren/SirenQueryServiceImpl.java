package com.example.waggle.domain.board.application.siren;

import com.example.waggle.domain.board.persistence.dao.siren.jpa.SirenRepository;
import com.example.waggle.domain.board.persistence.entity.ResolutionStatus;
import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.exception.object.handler.SirenHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SirenQueryServiceImpl implements SirenQueryService {

    private final SirenRepository sirenRepository;
    private final RecommendRepository recommendRepository;
    private final RedisService redisService;

    @Override
    public List<Siren> getAllSiren() {
        return sirenRepository.findAll();
    }

    @Override
    public Page<Siren> getPagedSirenList(Pageable pageable) {
        return sirenRepository.findAll(pageable);
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
        return sirenRepository.findByMemberUsername(username, pageable);
    }

    @Override
    public Page<Siren> getPagedSirenListByUserUrl(String userUrl, Pageable pageable) {
        return sirenRepository.findByMemberUserUrl(userUrl, pageable);
    }

    @Override
    public Page<Siren> getPagedSirenListByMemberId(Long memberId, Pageable pageable) {
        return sirenRepository.findByMemberId(memberId, pageable);
    }

    @Override
    public Page<Siren> getPagedSirenListByFilter(SirenFilterParam filterParam, Pageable pageable) {
        return sirenRepository.findSirensByFilter(filterParam, pageable);
    }

    @Override
    public Page<Siren> getPagedSirenListByCategory(SirenCategory category, Pageable pageable) {
        return sirenRepository.findByCategory(category, pageable);
    }

    @Override
    public Siren getSirenByBoardId(Long boardId) {
        return sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));
    }

    @Override
    public List<Siren> getRandomUnsolvedSirenList() {
        return sirenRepository.findRandomUnresolvedSirens();
    }

}
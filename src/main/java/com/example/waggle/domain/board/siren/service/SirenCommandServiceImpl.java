package com.example.waggle.domain.board.siren.service;

import static com.example.waggle.domain.board.service.BoardType.SIREN;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.exception.handler.SirenHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.siren.SirenRequest;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SirenCommandServiceImpl implements SirenCommandService {

    private final SirenRepository sirenRepository;
    private final RecommendRepository recommendRepository;
    private final BoardService boardService;
    private final CommentCommandService commentCommandService;
    private final MediaCommandService mediaCommandService;
    private final RedisService redisService;

    @Override
    public Long createSiren(SirenRequest createSirenRequest, Member member) {
        Siren siren = buildSiren(createSirenRequest, member);
        sirenRepository.save(siren);
        mediaCommandService.createMedia(createSirenRequest.getMediaList(), siren);
        return siren.getId();
    }

    @Override
    public Long updateSiren(Long boardId,
                            SirenRequest updateSirenRequest,
                            Member member) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(updateSirenRequest);
        mediaCommandService.updateMedia(updateSirenRequest.getMediaList(), siren);

        return siren.getId();
    }


    @Override
    public void convertStatus(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        switch (siren.getStatus()) {
            case RESOLVED -> siren.changeStatus(ResolutionStatus.UNRESOLVED);
            case UNRESOLVED -> siren.changeStatus(ResolutionStatus.RESOLVED);
            default -> throw new QuestionHandler(ErrorStatus.BOARD_INVALID_TYPE);
        }
    }

    @Override
    public void deleteSiren(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(boardId);

        sirenRepository.delete(siren);
    }

    @Override
    public void increaseSirenViewCount(Long boardId) {
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));
        siren.increaseViewCount();
    }

    @Override
    public void applyViewCountToRedis(Long boardId) {
        String viewCountKey = "viewCount::" + boardId;
        if (redisService.getValue(viewCountKey) != null) {
            redisService.increment(viewCountKey);
            return;
        }
        redisService.setData(
                viewCountKey,
                String.valueOf(sirenRepository.findViewCountByBoardId(boardId) + 1),
                Duration.ofMinutes(3)
        );
    }

    @Scheduled(cron = "0 0/3 * * * ?")
    @Override
    public void applyViewCountToRDB() {
        Set<String> viewCountKeys = redisService.keys("viewCount*");
        if (Objects.requireNonNull(viewCountKeys).isEmpty()) {
            return;
        }

        for (String viewCntKey : viewCountKeys) {
            Long boardId = extractBoardIdFromKey(viewCntKey);
            Long viewCount = Long.parseLong(redisService.getData(viewCntKey));

            sirenRepository.applyViewCntToRDB(boardId, viewCount);
            redisService.deleteData(viewCntKey);
            redisService.deleteData("board::" + boardId);
        }
    }

    private static Long extractBoardIdFromKey(String key) {
        return Long.parseLong(key.split("::")[1]);
    }

    private Siren buildSiren(SirenRequest createSirenRequest, Member member) {
        return Siren.builder()
                .title(createSirenRequest.getTitle())
                .contact(createSirenRequest.getContact())
                .content(createSirenRequest.getContent())
                .petBreed(createSirenRequest.getPetBreed())
                .petGender(Gender.valueOf(createSirenRequest.getPetGender()))
                .petAge(createSirenRequest.getPetAge())
                .lostDate(createSirenRequest.getLostDate())
                .lostLocate(createSirenRequest.getLostLocate())
                .category(SirenCategory.valueOf(createSirenRequest.getCategory()))
                .status(ResolutionStatus.UNRESOLVED)
                .member(member)
                .build();
    }
}
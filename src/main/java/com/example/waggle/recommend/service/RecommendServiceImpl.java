package com.example.waggle.recommend.service;

import com.example.waggle.board.Board;
import com.example.waggle.board.question.dto.AnswerDetailDto;
import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.dto.QuestionDetailDto;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.member.domain.Member;
import com.example.waggle.recommend.domain.Recommend;
import com.example.waggle.recommend.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.waggle.commons.exception.ErrorCode.CANNOT_RECOMMEND_MYSELF;
import static com.example.waggle.commons.exception.ErrorCode.RECOMMEND_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final UtilService utilService;

    @Override
    public void clickRecommend(Long boardId, BoardType boardType) {

        Board board = utilService.getBoard(boardId, boardType);
        Member member = utilService.getSignInMember();

        //check recommend board
        boolean check = recommendRepository
                .existsByMemberIdAndBoardId(member.getId(), board.getId());
        log.info("i recommended this board {}", check);

        //change state
        if (check) {
            //cancel recommend
            Recommend recommend = recommendRepository
                    .findRecommendByMemberIdAndBoardId(member.getId(), boardId)
                    .orElseThrow(() -> new CustomPageException(RECOMMEND_NOT_FOUND));
            recommendRepository.delete(recommend);
        }
        else{
            //recommend story as member
            if (board.getMember().equals(member)) {
                log.info("can't recommend");
                log.info("board Member {}", board.getMember().getUsername());
                throw new CustomPageException(CANNOT_RECOMMEND_MYSELF);
            }
            Recommend recommendBoard = Recommend.builder().board(board).member(member).build();
            recommendRepository.save(recommendBoard);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void checkRecommend(QuestionDetailDto questionDetailDto) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername()
                .equals(questionDetailDto.getUsername())) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), questionDetailDto.getId());
        }

        int count = recommendRepository.countByBoardId(questionDetailDto.getId());
        questionDetailDto.linkRecommend(count, recommendIt);
    }

    @Transactional(readOnly = true)
    @Override
    public void checkRecommend(AnswerDetailDto answerDetailDto) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername()
                .equals(answerDetailDto.getUsername())) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), answerDetailDto.getId());
        }

        int count = recommendRepository.countByBoardId(answerDetailDto.getId());
        answerDetailDto.linkRecommend(count, recommendIt);
    }

    @Transactional(readOnly = true)
    @Override
    public void checkRecommend(StoryDetailDto storyDetailDto) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername()
                .equals(storyDetailDto.getUsername())) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), storyDetailDto.getId());
        }

        int count = recommendRepository.countByBoardId(storyDetailDto.getId());
        log.info("count {}",count);
        storyDetailDto.linkRecommend(count, recommendIt);
    }


    //========== 단체 조회 시 =========
    // 단체 recommend 확인은 쿼리량을 줄이기 위함이다.
    @Transactional(readOnly = true)
    @Override
    public void checkRecommendQuestions(List<QuestionSummaryDto> questionViewDtoList) {
        for (QuestionSummaryDto questionViewDto : questionViewDtoList) {
            Member signInMember = utilService.getSignInMember();
            boolean recommendIt = false;
            //(login user == board writer) checking
            if (!signInMember.getUsername()
                    .equals(questionViewDto.getUsername())) {
                recommendIt = recommendRepository
                        .existsByMemberIdAndBoardId(signInMember.getId(), questionViewDto.getId());
            }
            int count = recommendRepository.countByBoardId(questionViewDto.getId());
            questionViewDto.linkRecommend(count, recommendIt);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public void checkRecommendAnswers(List<AnswerDetailDto> answerDetailDtoList) {
        for (AnswerDetailDto answerDetailDto : answerDetailDtoList) {
            checkRecommend(answerDetailDto);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public void checkRecommendStories(List<StorySummaryDto> storyViewDtoList) {
        for (StorySummaryDto storyViewDto : storyViewDtoList) {
            Member signInMember = utilService.getSignInMember();
            boolean recommendIt = false;
            //(login user == board writer) checking
            if (!signInMember.getUsername()
                    .equals(storyViewDto.getUsername())) {
                recommendIt = recommendRepository
                        .existsByMemberIdAndBoardId(signInMember.getId(), storyViewDto.getId());
            }
            int count = recommendRepository.countByBoardId(storyViewDto.getId());
            storyViewDto.linkRecommend(count, recommendIt);
        }
    }
}

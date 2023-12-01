package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.web.dto.answer.AnswerDetailDto;
import com.example.waggle.web.dto.question.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionDetailDto;
import com.example.waggle.web.dto.story.StorySummaryDto;
import com.example.waggle.web.dto.story.StoryDetailDto;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.domain.recommend.domain.Recommend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.waggle.global.exception.ErrorCode.CANNOT_RECOMMEND_MYSELF;
import static com.example.waggle.global.exception.ErrorCode.RECOMMEND_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final UtilService utilService;

    @Transactional
    @Override
    public void handleRecommendation(Long boardId, BoardType boardType) {
        Board board = utilService.getBoard(boardId, boardType);
        Member member = utilService.getSignInMember();
        boolean isRecommended = recommendRepository.existsByMemberIdAndBoardId(member.getId(), board.getId());
        if (isRecommended) {
            cancelRecommendation(member.getId(), boardId);
        }
        else{
            if (board.getMember().equals(member)) {
                throw new CustomPageException(CANNOT_RECOMMEND_MYSELF);
            }
            createRecommendation(board, member);
        }
    }

    private void cancelRecommendation(Long memberId, Long boardId) {
        Recommend recommend = recommendRepository
                .findRecommendByMemberIdAndBoardId(memberId, boardId)
                .orElseThrow(() -> new CustomPageException(RECOMMEND_NOT_FOUND));
        recommendRepository.delete(recommend);
    }

    private void createRecommendation(Board board, Member member) {
        Recommend recommend = Recommend.builder().board(board).member(member).build();
        recommendRepository.save(recommend);
    }


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

    @Override
    public void checkRecommend(StoryDetailDto storyDetailDto) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername().equals(storyDetailDto.getUsername())) {
            recommendIt = recommendRepository.existsByMemberIdAndBoardId(signInMember.getId(), storyDetailDto.getId());
        }

        int count = recommendRepository.countByBoardId(storyDetailDto.getId());
        storyDetailDto.linkRecommend(count, recommendIt);
    }

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

    @Override
    public void checkRecommendAnswers(List<AnswerDetailDto> answerDetailDtoList) {
        for (AnswerDetailDto answerDetailDto : answerDetailDtoList) {
            checkRecommend(answerDetailDto);
        }
    }

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

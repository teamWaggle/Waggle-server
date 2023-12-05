package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.util.service.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecommendQueryServiceImpl implements RecommendQueryService{

    private final UtilService utilService;
    private final RecommendRepository recommendRepository;

    @Override
    public boolean checkRecommend(Long boardId, String boardWriter) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername()
                .equals(boardWriter)) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), boardId);
        }
        return recommendIt;
    }

    @Override
    public int countRecommend(Long boardId) {
        return recommendRepository.countByBoardId(boardId);
    }

//    @Override
//    public void checkRecommend(AnswerDetailDto answerDetailDto) {
//        Member signInMember = utilService.getSignInMember();
//        boolean recommendIt = false;
//        //(login user == board writer) checking
//        if (!signInMember.getUsername()
//                .equals(answerDetailDto.getUsername())) {
//            recommendIt = recommendRepository
//                    .existsByMemberIdAndBoardId(signInMember.getId(), answerDetailDto.getId());
//        }
//
//        int count = recommendRepository.countByBoardId(answerDetailDto.getId());
//        answerDetailDto.linkRecommend(count, recommendIt);
//    }
//
//    @Override
//    public void checkRecommend(StoryDetailDto storyDetailDto) {
//        Member signInMember = utilService.getSignInMember();
//        boolean recommendIt = false;
//        //(login user == board writer) checking
//        if (!signInMember.getUsername().equals(storyDetailDto.getUsername())) {
//            recommendIt = recommendRepository.existsByMemberIdAndBoardId(signInMember.getId(), storyDetailDto.getId());
//        }
//
//        int count = recommendRepository.countByBoardId(storyDetailDto.getId());
//        storyDetailDto.linkRecommend(count, recommendIt);
//    }
//
//    @Override
//    public void checkRecommendQuestions(List<QuestionSummaryDto> questionViewDtoList) {
//        for (QuestionSummaryDto questionViewDto : questionViewDtoList) {
//            Member signInMember = utilService.getSignInMember();
//            boolean recommendIt = false;
//            //(login user == board writer) checking
//            if (!signInMember.getUsername()
//                    .equals(questionViewDto.getUsername())) {
//                recommendIt = recommendRepository
//                        .existsByMemberIdAndBoardId(signInMember.getId(), questionViewDto.getId());
//            }
//            int count = recommendRepository.countByBoardId(questionViewDto.getId());
//            questionViewDto.linkRecommend(count, recommendIt);
//        }
//    }
//
//    @Override
//    public void checkRecommendAnswers(List<AnswerDetailDto> answerDetailDtoList) {
//        for (AnswerDetailDto answerDetailDto : answerDetailDtoList) {
//            checkRecommend(answerDetailDto);
//        }
//    }
//
//    @Override
//    public void checkRecommendStories(List<StorySummaryDto> storyViewDtoList) {
//        for (StorySummaryDto storyViewDto : storyViewDtoList) {
//            Member signInMember = utilService.getSignInMember();
//            boolean recommendIt = false;
//            //(login user == board writer) checking
//            if (!signInMember.getUsername()
//                    .equals(storyViewDto.getUsername())) {
//                recommendIt = recommendRepository
//                        .existsByMemberIdAndBoardId(signInMember.getId(), storyViewDto.getId());
//            }
//            int count = recommendRepository.countByBoardId(storyViewDto.getId());
//            storyViewDto.linkRecommend(count, recommendIt);
//        }
//    }
}

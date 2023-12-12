package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;

import java.util.List;

public interface RecommendQueryService {
//    void checkRecommend(QuestionDetailDto questionDetailDto);
//
//    void checkRecommend(AnswerDetailDto answerDetailDto);
//
//    void checkRecommend(StoryDetailDto storyDetailDto);
//
//    void checkRecommendQuestions(List<QuestionSummaryDto> questionViewDtoList);
//
//    void checkRecommendAnswers(List<AnswerDetailDto> answerDetailDtoList);
//
//    void checkRecommendStories(List<StorySummaryDto> storyViewDtoList);

    boolean checkRecommend(Long boardId, String boardWriter);

    int countRecommend(Long boardId);

    List<Member> getRecommendingMembers(Long boardId);
}

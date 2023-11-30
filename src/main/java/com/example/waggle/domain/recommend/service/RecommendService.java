package com.example.waggle.domain.recommend.service;

import com.example.waggle.web.dto.answer.AnswerDetailDto;
import com.example.waggle.web.dto.question.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionDetailDto;
import com.example.waggle.web.dto.story.StorySummaryDto;
import com.example.waggle.web.dto.story.StoryDetailDto;
import com.example.waggle.global.util.service.BoardType;

import java.util.List;

public interface RecommendService {

    void handleRecommendation(Long boardId, BoardType boardType);

    void checkRecommend(QuestionDetailDto questionDetailDto);

    void checkRecommend(AnswerDetailDto answerDetailDto);

    void checkRecommend(StoryDetailDto storyDetailDto);

    void checkRecommendQuestions(List<QuestionSummaryDto> questionViewDtoList);

    void checkRecommendAnswers(List<AnswerDetailDto> answerDetailDtoList);

    void checkRecommendStories(List<StorySummaryDto> storyViewDtoList);

}

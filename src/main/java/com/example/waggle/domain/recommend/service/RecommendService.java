package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.question.dto.AnswerDetailDto;
import com.example.waggle.domain.board.question.dto.QuestionSummaryDto;
import com.example.waggle.domain.board.question.dto.QuestionDetailDto;
import com.example.waggle.domain.board.story.dto.StorySummaryDto;
import com.example.waggle.domain.board.story.dto.StoryDetailDto;
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

package com.example.waggle.recommend.service;

import com.example.waggle.board.question.dto.AnswerDetailDto;
import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.dto.QuestionDetailDto;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.commons.util.service.BoardType;

import java.util.List;

public interface RecommendService {

    public void clickRecommend(Long boardId, BoardType boardType);

    public void checkRecommend(QuestionDetailDto questionDetailDto);

    public void checkRecommend(AnswerDetailDto answerDetailDto);

    public void checkRecommend(StoryDetailDto storyDetailDto);

    public void checkRecommendQuestions(List<QuestionSummaryDto> questionViewDtoList);

    public void checkRecommendAnswers(List<AnswerDetailDto> answerDetailDtoList);

    public void checkRecommendStories(List<StorySummaryDto> storyViewDtoList);

}

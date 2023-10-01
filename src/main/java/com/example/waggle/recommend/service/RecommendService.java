package com.example.waggle.recommend.service;

import com.example.waggle.board.Board;
import com.example.waggle.recommend.domain.Recommend;
import com.example.waggle.member.domain.Member;
import com.example.waggle.board.question.dto.AnswerViewDto;
import com.example.waggle.board.question.dto.QuestionSimpleViewDto;
import com.example.waggle.board.question.dto.QuestionViewDto;
import com.example.waggle.board.story.dto.StorySimpleViewDto;
import com.example.waggle.board.story.dto.StoryViewDto;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.recommend.repository.RecommendRepository;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.commons.util.service.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.waggle.commons.exception.ErrorCode.CANNOT_RECOMMEND_MYSELF;
import static com.example.waggle.commons.exception.ErrorCode.RECOMMEND_NOT_FOUND;

public interface RecommendService {

    public void clickRecommend(Long boardId, BoardType boardType);

    public void checkRecommend(QuestionViewDto questionViewDto);

    public void checkRecommend(AnswerViewDto answerViewDto);

    public void checkRecommend(StoryViewDto storyViewDto);

    public void checkRecommendQuestions(List<QuestionSimpleViewDto> questionViewDtoList);

    public void checkRecommendAnswers(List<AnswerViewDto> answerViewDtoList);

    public void checkRecommendStories(List<StorySimpleViewDto> storyViewDtoList);

}

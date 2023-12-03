package com.example.waggle.web.converter;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.web.dto.question.QuestionResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class QuestionConverter {
    public static QuestionResponse.QuestionSummaryDto toQuestionSummaryDto(Question question) {
        return QuestionResponse.QuestionSummaryDto.builder()
                .id(question.getId())
                .username(question.getMember().getUsername())
                .title(question.getTitle())
                .createTime(question.getCreatedDate())
                .hashtags(question.getBoardHashtags().stream()
                        .map(h->h.getHashtag().getTag()).collect(Collectors.toList()))
                .build();
    }

    public static QuestionResponse.QuestionsListDto toQuestionsListDto(Page<Question> questionPage) {
        List<QuestionResponse.QuestionSummaryDto> questionsSummaryDtoList = questionPage.stream()
                .map(QuestionConverter::toQuestionSummaryDto)
                .collect(Collectors.toList());

        return QuestionResponse.QuestionsListDto.builder()
                .questionsList(questionsSummaryDtoList)
                .totalQuestions(questionPage.getTotalElements())
                .isFirst(questionPage.isFirst())
                .isLast(questionPage.isLast())
                .build();
    }

}

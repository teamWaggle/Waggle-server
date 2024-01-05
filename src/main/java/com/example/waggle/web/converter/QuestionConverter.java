package com.example.waggle.web.converter;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.question.QuestionResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {
    public static QuestionResponse.SummaryDto toSummaryDto(Question question) {
        return QuestionResponse.SummaryDto.builder()
                .id(question.getId())
                .username(question.getMember().getUsername())
                .profileImg(MediaUtil.getProfile(question.getMember()))
                .title(question.getTitle())
                .createTime(question.getCreatedDate())
                .hashtags(question.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getContent()).collect(Collectors.toList()))
                .build();
    }

    public static QuestionResponse.ListDto toListDto(Page<Question> questionPage) {
        List<QuestionResponse.SummaryDto> questionsSummaryDtoList = questionPage.stream()
                .map(QuestionConverter::toSummaryDto)
                .collect(Collectors.toList());

        return QuestionResponse.ListDto.builder()
                .questionsList(questionsSummaryDtoList)
                .totalQuestions(questionPage.getTotalElements())
                .isFirst(questionPage.isFirst())
                .isLast(questionPage.isLast())
                .build();
    }

    public static QuestionResponse.DetailDto toDetailDto(Question question) {
        return QuestionResponse.DetailDto.builder()
                .id(question.getId())
                .content(question.getContent())
                .username(question.getMember().getUsername())
                .profileImg(MediaUtil.getProfile(question.getMember()))
                .title(question.getTitle())
                .createDate(question.getCreatedDate())
                .hashtags(question.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getContent()).collect(Collectors.toList()))
                .medias(MediaUtil.getBoardMedias(question))
                .build();
    }
}

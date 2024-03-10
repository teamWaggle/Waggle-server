package com.example.waggle.web.converter;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.question.QuestionResponse;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryListDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {
    public static QuestionSummaryDto toSummaryDto(Question question) {
        return QuestionSummaryDto.builder()
                .boardId(question.getId())
                .title(question.getTitle())
                .status(question.getStatus())
                .createdDate(question.getCreatedDate())
                .hashtagList(question.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getContent()).collect(Collectors.toList()))
                .member(MemberConverter.toMemberSummaryDto(question.getMember()))
                .build();
    }

    public static QuestionSummaryListDto toListDto(Page<Question> questionPage) {
        List<QuestionSummaryDto> questionsSummaryDtoList = questionPage.stream()
                .map(QuestionConverter::toSummaryDto)
                .collect(Collectors.toList());

        return QuestionSummaryListDto.builder()
                .questionList(questionsSummaryDtoList)
                .questionCount(questionPage.getTotalElements())
                .isFirst(questionPage.isFirst())
                .isLast(questionPage.isLast())
                .build();
    }

    public static QuestionResponse.QuestionDetailDto toDetailDto(Question question) {
        return QuestionResponse.QuestionDetailDto.builder()
                .boardId(question.getId())
                .title(question.getTitle())
                .status(question.getStatus())
                .content(question.getContent())
                .createdDate(question.getCreatedDate())
                .hashtagList(question.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getContent()).collect(Collectors.toList()))
                .mediaList(MediaUtil.getBoardMedias(question))
                .member(MemberConverter.toMemberSummaryDto(question.getMember()))
                .build();
    }
}

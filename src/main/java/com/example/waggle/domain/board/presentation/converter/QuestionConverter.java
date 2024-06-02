package com.example.waggle.domain.board.presentation.converter;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionResponse;
import com.example.waggle.domain.board.presentation.dto.question.QuestionResponse.QuestionSummaryDto;
import com.example.waggle.domain.board.presentation.dto.question.QuestionResponse.QuestionSummaryListDto;
import com.example.waggle.domain.board.presentation.dto.question.QuestionResponse.RepresentativeQuestionDto;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.PageUtil;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {
    public static QuestionSummaryDto toSummaryDto(Question question) {
        return QuestionSummaryDto.builder()
                .boardId(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .status(question.getStatus())
                .createdDate(question.getCreatedDate())
                .hashtagList(question.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getContent()).collect(Collectors.toList()))
                .build();
    }

    public static QuestionSummaryListDto toListDto(Page<Question> questionPage) {
        List<QuestionSummaryDto> questionsSummaryDtoList = questionPage.stream()
                .map(QuestionConverter::toSummaryDto)
                .collect(Collectors.toList());

        return QuestionSummaryListDto.builder()
                .questionList(questionsSummaryDtoList)
                .nextPageParam(PageUtil.countNextPage(questionPage))
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
                .viewCount(question.getViewCount())
                .commentCount(question.getComments().size())
                .build();
    }

    public static RepresentativeQuestionDto toRepresentativeQuestionDto(List<Question> questionList) {
        List<QuestionSummaryDto> collect = questionList.stream()
                .map(QuestionConverter::toSummaryDto).collect(Collectors.toList());
        return RepresentativeQuestionDto.builder()
                .questionList(collect)
                .build();
    }

}

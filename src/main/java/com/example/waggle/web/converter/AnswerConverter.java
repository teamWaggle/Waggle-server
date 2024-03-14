package com.example.waggle.web.converter;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.answer.AnswerResponse.AnswerListDto;
import com.example.waggle.web.dto.answer.AnswerResponse.AnswerViewDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class AnswerConverter {

    public static AnswerViewDto toAnswerViewDto(Answer answer) {
        return AnswerViewDto.builder()
                .boardId(answer.getId())
                .content(answer.getContent())
                .createdDate(answer.getCreatedDate())
                .mediaList(MediaUtil.getBoardMedias(answer))
                .member(MemberConverter.toMemberSummaryDto(answer.getMember()))
                .build();
    }

    public static AnswerListDto toAnswerListDto(Page<Answer> pagedAnswer) {
        List<AnswerViewDto> collect = pagedAnswer.stream()
                .map(AnswerConverter::toAnswerViewDto).collect(Collectors.toList());
        return AnswerListDto.builder()
                .answerList(collect)
                .answerCount(pagedAnswer.getTotalElements())
                .isFirst(pagedAnswer.isFirst())
                .isLast(pagedAnswer.isLast())
                .build();
    }
}

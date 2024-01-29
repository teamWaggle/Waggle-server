package com.example.waggle.web.converter;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.answer.AnswerResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class AnswerConverter {

    public static AnswerResponse.ViewDto toViewDto(Answer answer) {
        return AnswerResponse.ViewDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .username(answer.getMember().getUsername())
                .profileImg(MediaUtil.getProfileImg(answer.getMember()))
                .createDate(answer.getCreatedDate())
                .medias(MediaUtil.getBoardMedias(answer))
                .build();
    }

    public static AnswerResponse.ListDto toListDto(Page<Answer> pagedAnswer) {
        List<AnswerResponse.ViewDto> collect = pagedAnswer.stream()
                .map(AnswerConverter::toViewDto).collect(Collectors.toList());
        return AnswerResponse.ListDto.builder()
                .AnswerList(collect)
                .totalAnswers(pagedAnswer.getTotalElements())
                .isFirst(pagedAnswer.isFirst())
                .isLast(pagedAnswer.isLast())
                .build();
    }
}

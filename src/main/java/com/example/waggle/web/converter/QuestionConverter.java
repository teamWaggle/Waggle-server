package com.example.waggle.web.converter;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.question.QuestionResponse;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryListDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class QuestionConverter {
    public static QuestionSummaryDto toSummaryDto(Question question) {
        return QuestionSummaryDto.builder()
                .boardId(question.getId())
                .title(question.getTitle())
                .createdDate(question.getCreatedDate())
                .hashtagList(question.getBoardHashtags().stream()
                        .map(h -> h.getHashtag().getContent()).collect(Collectors.toList()))
                .member(MemberConverter.toMemberSummaryDto(question.getMember()))
                .isOwner(question.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
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
                .content(question.getContent())
                .title(question.getTitle())
                .createdDate(question.getCreatedDate())
                .hashtagList(question.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getContent()).collect(Collectors.toList()))
                .mediaList(MediaUtil.getBoardMedias(question))
                .member(MemberConverter.toMemberSummaryDto(question.getMember()))
                .isOwner(question.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
                .build();
    }
}

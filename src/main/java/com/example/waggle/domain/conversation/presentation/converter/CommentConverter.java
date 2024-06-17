package com.example.waggle.domain.conversation.presentation.converter;

import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.conversation.presentation.dto.CommentResponse.*;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.global.util.PageUtil;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static CommentViewDto toCommentViewDto(Comment comment) {
        return CommentViewDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .member(MemberConverter.toMemberSummaryDto(comment.getMember()))
                .build();
    }

    public static CommentListDto toCommentListDto(Page<Comment> pagedComment) {
        List<CommentViewDto> collect = pagedComment.stream()
                .map(CommentConverter::toCommentViewDto).collect(Collectors.toList());
        return CommentListDto.builder()
                .commentList(collect)
                .nextPageParam(PageUtil.countNextPage(pagedComment))
                .totalCount(pagedComment.getTotalElements())
                .build();
    }

    public static QuestionCommentListDto toQuestionCommentListDto(Page<QuestionCommentViewDto> pagedComments) {
        return QuestionCommentListDto.builder()
                .commentList(pagedComments.getContent())
                .nextPageParam(PageUtil.countNextPage(pagedComments))
                .build();
    }

    public static SirenCommentListDto toSirenCommentListDto(Page<SirenCommentViewDto> pagedComments) {
        return SirenCommentListDto.builder()
                .commentList(pagedComments.getContent())
                .nextPageParam(PageUtil.countNextPage(pagedComments))
                .build();
    }
}

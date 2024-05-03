package com.example.waggle.web.converter;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.global.util.PageUtil;
import com.example.waggle.web.dto.comment.CommentResponse.CommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.CommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentViewDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

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

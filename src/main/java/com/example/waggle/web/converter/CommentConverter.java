package com.example.waggle.web.converter;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.comment.CommentResponse.CommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.CommentViewDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static CommentViewDto toCommentViewDto(Comment comment) {
        return CommentViewDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .mentionedMemberList(comment.getMentions().stream()
                        .map(mention -> mention.getMentionedNickname()).collect(Collectors.toList()))
                .member(MemberConverter.toMemberSummaryDto(comment.getMember()))
                .isOwner(comment.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
                .build();
    }

    public static CommentListDto toCommentListDto(Page<Comment> pagedComment) {
        List<CommentViewDto> collect = pagedComment.stream()
                .map(CommentConverter::toCommentViewDto).collect(Collectors.toList());
        return CommentListDto.builder()
                .commentList(collect)
                .isFirst(pagedComment.isFirst())
                .isLast(pagedComment.isLast())
                .commentCount(pagedComment.getTotalElements())
                .build();
    }
}

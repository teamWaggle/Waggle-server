package com.example.waggle.web.converter;

import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.web.dto.comment.CommentResponse;
import com.example.waggle.web.dto.reply.ReplyViewDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static CommentResponse.ViewDto toViewDto(Comment comment) {
        return CommentResponse.ViewDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getMember().getUsername())
                .replies(comment.getReplies().stream()
                        .map(r -> ReplyViewDto.toDto(r)).collect(Collectors.toList()))
                .build();
    }

    public static CommentResponse.ListDto toListDto(Page<Comment> pagedComment) {
        List<CommentResponse.ViewDto> collect = pagedComment.stream()
                .map(CommentConverter::toViewDto).collect(Collectors.toList());
        return CommentResponse.ListDto.builder()
                .commentList(collect)
                .isFirst(pagedComment.isFirst())
                .isLast(pagedComment.isLast())
                .totalComments(pagedComment.getTotalElements())
                .build();
    }
}

package com.example.waggle.dto.board.comment;

import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.dto.board.reply.ReplyViewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentViewDto {

    private Long id;
    private String content;
    private int orders;
    @Builder.Default
    private List<ReplyViewDto> replies = new ArrayList<>();

    static public CommentViewDto toDto(Comment comment) {
        return CommentViewDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .orders(comment.getOrders())
                .replies(comment.getReplies().stream()
                        .map(r -> ReplyViewDto.toDto(r)).collect(Collectors.toList()))
                .build();
    }
}

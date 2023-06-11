package com.example.waggle.dto.board;

import com.example.waggle.domain.board.comment.Comment;
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
public class CommentDto {

    private Long id;
    private String content;
    private int orders;
    @Builder.Default
    private List<ReplyDto> replies = new ArrayList<>();

    static public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .orders(comment.getOrders())
                .replies(comment.getReplies().stream()
                        .map(r -> ReplyDto.toDto(r)).collect(Collectors.toList()))
                .build();
    }
}

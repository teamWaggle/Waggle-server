package com.example.waggle.dto.board.comment;

import com.example.waggle.domain.board.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentWriteDto {
    private String content;
    private String username;
    private Long boardId;

}

package com.example.waggle.web.dto.comment;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.member.entity.Member;
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

    public Comment toEntity(Member member, Board board) {
        return Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();
    }

}

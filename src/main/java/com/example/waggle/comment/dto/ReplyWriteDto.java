package com.example.waggle.comment.dto;

import com.example.waggle.comment.domain.Comment;
import com.example.waggle.comment.domain.Reply;
import com.example.waggle.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyWriteDto {

    private String content;
    @Builder.Default
    private List<String> mentions = new ArrayList<>();
    private Long commentId;

    public Reply toEntity(Member member, Comment comment) {
        return Reply.builder()
                .member(member)
                .comment(comment)
                .content(content)
                .build();
    }

}

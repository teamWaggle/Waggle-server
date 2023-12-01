package com.example.waggle.web.dto.reply;

import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.member.domain.Member;
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

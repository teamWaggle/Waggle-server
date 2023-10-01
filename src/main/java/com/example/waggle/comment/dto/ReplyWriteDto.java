package com.example.waggle.comment.dto;

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
    private List<String> mentionMembers = new ArrayList<>();

    public Reply toEntity(Member member) {
        return Reply.builder()
                .content(content)
                .member(member)
                .build();
    }

}
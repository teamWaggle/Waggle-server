package com.example.waggle.comment.dto;

import com.example.waggle.comment.domain.Reply;
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
public class ReplyViewDto {

    private Long id;
    private String content;
    private String username;
    @Builder.Default
    private List<String> mentionMembers = new ArrayList<>();

    static public ReplyViewDto toDto(Reply reply) {
        return ReplyViewDto.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .username(reply.getMember().getUsername())
                .mentionMembers(reply.getMemberMentions().stream()
                        .map(m->m.getUsername()).collect(Collectors.toList()))
                .build();
    }
}

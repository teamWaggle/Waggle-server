package com.example.waggle.dto.board.reply;

import com.example.waggle.domain.board.comment.Reply;
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
    @Builder.Default
    private List<String> mentionMembers = new ArrayList<>();

    static public ReplyViewDto toDto(Reply reply) {
        return ReplyViewDto.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .mentionMembers(reply.getMemberMentions().stream()
                        .map(m->m.getUsername()).collect(Collectors.toList()))
                .build();
    }
}

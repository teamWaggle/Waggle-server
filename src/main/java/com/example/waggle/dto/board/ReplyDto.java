package com.example.waggle.dto.board;

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
public class ReplyDto {

    private Long id;
    private String content;
    private int orders;
    @Builder.Default
    private List<String> mentionMembers = new ArrayList<>();

    static public ReplyDto toDto(Reply reply) {
        return ReplyDto.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .orders(reply.getOrders())
                .mentionMembers(reply.getMemberMentions().stream()
                        .map(m->m.getUsername()).collect(Collectors.toList()))
                .build();
    }
}

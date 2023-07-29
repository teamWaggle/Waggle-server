package com.example.waggle.dto.board.reply;

import com.example.waggle.domain.board.comment.Reply;
import com.example.waggle.domain.member.Member;
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

}

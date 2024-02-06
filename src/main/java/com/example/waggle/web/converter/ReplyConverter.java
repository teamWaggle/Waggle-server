package com.example.waggle.web.converter;

import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.member.MemberResponse;
import com.example.waggle.web.dto.reply.ReplyResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyConverter {
    public static ReplyResponse.ViewDto toViewDto(Reply reply) {
        Member member = reply.getMember();
        return ReplyResponse.ViewDto.builder()
                .id(reply.getId())
                .member(MemberResponse.SummaryDto.builder()
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .profileImgUrl(MediaUtil.getProfileImg(member))
                        .build())
                .content(reply.getContent())
                .mentionedNickname(reply.getMentions().stream()
                        .map(mention -> mention.getMentionedNickname()).collect(Collectors.toList()))
                .build();
    }

    public static ReplyResponse.ListDto toListDto(Page<Reply> pagedReply) {
        List<ReplyResponse.ViewDto> collect = pagedReply.stream()
                .map(ReplyConverter::toViewDto).collect(Collectors.toList());
        return ReplyResponse.ListDto.builder()
                .replyList(collect)
                .isFirst(pagedReply.isFirst())
                .isLast(pagedReply.isLast())
                .totalReplies(pagedReply.getTotalElements())
                .build();
    }
}

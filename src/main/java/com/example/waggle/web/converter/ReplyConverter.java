package com.example.waggle.web.converter;

import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.reply.ReplyResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyConverter {
    public static ReplyResponse.ViewDto toViewDto(Reply reply) {
        return ReplyResponse.ViewDto.builder()
                .id(reply.getId())
                .member(MemberConverter.toMemberSummaryDto(reply.getMember()))
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .mentionedNickname(reply.getMentions().stream()
                        .map(mention -> mention.getMentionedNickname()).collect(Collectors.toList()))
                .isMine(reply.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
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

package com.example.waggle.web.converter;

import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.reply.ReplyResponse.ReplyListDto;
import com.example.waggle.web.dto.reply.ReplyResponse.ReplyViewDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyConverter {
    public static ReplyViewDto toReplyViewDto(Reply reply) {
        return ReplyViewDto.builder()
                .replyId(reply.getId())
                .member(MemberConverter.toMemberSummaryDto(reply.getMember()))
                .isOwner(reply.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .build();
    }

    public static ReplyListDto toReplyListDto(Page<Reply> pagedReply) {
        List<ReplyViewDto> collect = pagedReply.stream()
                .map(ReplyConverter::toReplyViewDto).collect(Collectors.toList());
        return ReplyListDto.builder()
                .replyList(collect)
                .isFirst(pagedReply.isFirst())
                .isLast(pagedReply.isLast())
                .replyCount(pagedReply.getTotalElements())
                .build();
    }
}

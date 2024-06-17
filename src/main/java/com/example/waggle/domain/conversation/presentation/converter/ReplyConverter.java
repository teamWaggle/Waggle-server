package com.example.waggle.domain.conversation.presentation.converter;

import com.example.waggle.domain.conversation.persistence.entity.Reply;
import com.example.waggle.domain.conversation.presentation.dto.ReplyResponse.ReplyListDto;
import com.example.waggle.domain.conversation.presentation.dto.ReplyResponse.ReplyViewDto;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyConverter {
    public static ReplyViewDto toReplyViewDto(Reply reply) {
        return ReplyViewDto.builder()
                .replyId(reply.getId())
                .member(MemberConverter.toMemberSummaryDto(reply.getMember()))
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

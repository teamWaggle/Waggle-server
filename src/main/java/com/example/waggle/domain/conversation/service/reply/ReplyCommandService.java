package com.example.waggle.domain.conversation.service.reply;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.reply.ReplyRequest;

public interface ReplyCommandService {
    Long createReply(Long commentId, ReplyRequest.Post replyWriteDto);

    Long createReply(Long commentId, Member member, ReplyRequest.Post replyWriteDto);

    Long updateReply(Long replyId, ReplyRequest.Post replyWriteDto);

    Long updateReply(Long replyId, Member member, ReplyRequest.Post replyWriteDto);

    void deleteReply(Long replyId);

    void deleteReply(Long replyId, Member member);


}

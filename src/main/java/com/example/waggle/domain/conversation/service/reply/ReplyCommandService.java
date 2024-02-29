package com.example.waggle.domain.conversation.service.reply;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.reply.ReplyRequest;

public interface ReplyCommandService {

    Long createReply(Long commentId, ReplyRequest createReplyRequest, Member member);

    Long updateReply(Long replyId, ReplyRequest updateReplyRequest, Member member);

    void deleteReply(Long replyId, Member member);

}

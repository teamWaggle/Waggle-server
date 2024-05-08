package com.example.waggle.domain.conversation.application.reply;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.conversation.presentation.dto.reply.ReplyRequest;

public interface ReplyCommandService {

    Long createReply(Long commentId, ReplyRequest createReplyRequest, Member member);

    Long updateReply(Long replyId, ReplyRequest updateReplyRequest, Member member);

    void deleteReply(Long replyId, Member member);

}

package com.example.waggle.domain.conversation.application.reply;

import com.example.waggle.domain.conversation.presentation.dto.ConversationRequest;
import com.example.waggle.domain.member.persistence.entity.Member;

public interface ReplyCommandService {

    Long createReply(Long commentId, ConversationRequest createReplyRequest, Member member);

    Long updateReply(Long replyId, ConversationRequest updateReplyRequest, Member member);

    void deleteReply(Long replyId, Member member);

    void deleteReplyByAdmin(Long replyId, Member member);

}

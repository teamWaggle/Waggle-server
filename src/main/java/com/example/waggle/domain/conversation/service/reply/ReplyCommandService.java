package com.example.waggle.domain.conversation.service.reply;

import com.example.waggle.web.dto.reply.ReplyRequest;

public interface ReplyCommandService {
    Long createReply(Long commentId, ReplyRequest createReplyRequest);

    Long createReplyByUsername(Long commentId, ReplyRequest createReplyRequest, String username);

    Long updateReply(Long replyId, ReplyRequest updateReplyRequest);

    Long updateReplyByUsername(Long replyId, String username, ReplyRequest updateReplyRequest);

    void deleteReply(Long replyId);

    void deleteReply(Long replyId, String username);


}

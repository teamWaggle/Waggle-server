package com.example.waggle.domain.comment.service.reply;

import com.example.waggle.web.dto.reply.ReplyRequest;

public interface ReplyCommandService {
    Long createReply(Long commentId, ReplyRequest.Post replyWriteDto);

    Long createReply(Long commentId, ReplyRequest.Post replyWriteDto, String username);

    Long updateReply(Long replyId, ReplyRequest.Post replyWriteDto);

    void deleteReply(Long replyId);


}

package com.example.waggle.domain.conversation.service.reply;

import com.example.waggle.web.dto.reply.ReplyRequest.ReplyCreateDto;

public interface ReplyCommandService {
    Long createReply(Long commentId, ReplyCreateDto replyWriteDto);

    Long createReplyByUsername(Long commentId, ReplyCreateDto replyWriteDto, String username);

    Long updateReply(Long replyId, ReplyCreateDto replyWriteDto);

    Long updateReplyByUsername(Long replyId, String username, ReplyCreateDto replyWriteDto);

    void deleteReply(Long replyId);

    void deleteReply(Long replyId, String username);


}

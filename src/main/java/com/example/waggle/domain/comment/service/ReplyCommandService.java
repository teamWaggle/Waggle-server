package com.example.waggle.domain.comment.service;

import com.example.waggle.web.dto.reply.ReplyWriteDto;

public interface ReplyCommandService {
    Long createReply(Long commentId, ReplyWriteDto replyWriteDto);

    Long updateReply(Long replyId, ReplyWriteDto replyWriteDto);

    void deleteReply(Long replyId);


}

package com.example.waggle.domain.comment.service;


import com.example.waggle.domain.comment.dto.ReplyViewDto;
import com.example.waggle.domain.comment.dto.ReplyWriteDto;

import java.util.List;

public interface ReplyService {
    List<ReplyViewDto> getReplies(Long commentId);

    Long createReply(Long commentId, ReplyWriteDto replyWriteDto);

    Long updateReply(Long replyId, ReplyWriteDto replyWriteDto);

    void deleteReply(Long replyId);

    boolean validateMember(Long replyId);
}

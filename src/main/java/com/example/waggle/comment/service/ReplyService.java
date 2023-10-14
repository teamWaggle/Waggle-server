package com.example.waggle.comment.service;


import com.example.waggle.comment.dto.ReplyViewDto;
import com.example.waggle.comment.dto.ReplyWriteDto;

import java.util.List;

public interface ReplyService {
    List<ReplyViewDto> getReplies(Long commentId);

    Long createReply(Long commentId, ReplyWriteDto replyWriteDto);

    Long updateReply(Long replyId, ReplyWriteDto replyWriteDto);

    void deleteReply(Long replyId);

    boolean validateMember(Long replyId);
}

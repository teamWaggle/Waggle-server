package com.example.waggle.comment.service;


import com.example.waggle.comment.dto.CommentViewDto;
import com.example.waggle.comment.dto.ReplyViewDto;
import com.example.waggle.comment.dto.ReplyWriteDto;

import java.util.List;

public interface ReplyService {
    public List<ReplyViewDto> findReplies(Long commentId);

    public Long saveReply(CommentViewDto commentViewDto, ReplyWriteDto replyWriteDto);

    public boolean checkMember(ReplyViewDto viewDto);

    public Long changeReply(ReplyViewDto replyViewDto, ReplyWriteDto replyWriteDto);

    public void deleteReply(ReplyViewDto viewDto);
}

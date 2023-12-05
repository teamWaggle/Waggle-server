package com.example.waggle.domain.comment.service.reply;

import com.example.waggle.domain.comment.entity.Reply;

import java.util.List;

public interface ReplyQueryService {
    List<Reply> getReplies(Long commentId);
    boolean validateMember(Long replyId);
}

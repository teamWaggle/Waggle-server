package com.example.waggle.domain.comment.service.reply;

import com.example.waggle.web.dto.reply.ReplyViewDto;

import java.util.List;

public interface ReplyQueryService {
    List<ReplyViewDto> getReplies(Long commentId);
    boolean validateMember(Long replyId);
}

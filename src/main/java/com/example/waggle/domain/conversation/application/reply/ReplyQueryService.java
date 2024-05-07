package com.example.waggle.domain.conversation.application.reply;

import com.example.waggle.domain.conversation.persistence.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyQueryService {
    List<Reply> getReplies(Long commentId);

    Page<Reply> getPagedReplies(Long commentId, Pageable pageable);
}

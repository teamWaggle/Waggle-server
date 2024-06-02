package com.example.waggle.domain.conversation.application.reply;

import com.example.waggle.domain.conversation.persistence.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyQueryService {

    Page<Reply> getPagedReplies(Long commentId, Pageable pageable);
}

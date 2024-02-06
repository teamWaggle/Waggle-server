package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.conversation.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentQueryService {
    List<Comment> getComments(Long boardId);

    boolean validateMember(Long commentId);

    Page<Comment> getPagedComments(Long boardId, Pageable pageable);
}

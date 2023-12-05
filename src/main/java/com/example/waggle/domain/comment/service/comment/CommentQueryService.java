package com.example.waggle.domain.comment.service.comment;

import com.example.waggle.domain.comment.entity.Comment;

import java.util.List;

public interface CommentQueryService {
    List<Comment> getComments(Long boardId);

    boolean validateMember(Long commentId);
}

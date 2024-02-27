package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.web.dto.comment.CommentRequest;

public interface CommentCommandService {
    Long createComment(Long boardId, CommentRequest createCommentRequest);

    Long createCommentByUsername(Long boardId, CommentRequest createCommentRequest, String username);

    Long updateComment(Long commentId, CommentRequest updateCommentRequest);

    Long updateCommentByUsername(Long commentId, String username, CommentRequest updateCommentRequest);

    void deleteComment(Long commentId);

    void deleteCommentByUsername(Long commentId, String username);

    void deleteCommentForHardReset(Long commentId);

}

package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.web.dto.comment.CommentRequest;

public interface CommentCommandService {
    Long createComment(Long boardId, CommentRequest.Post commentWriteDto);

    Long createCommentByUsername(Long boardId, CommentRequest.Post commentWriteDto, String username);

    Long updateComment(Long commentId, CommentRequest.Post commentWriteDto);

    Long updateCommentByUsername(Long commentId, String username, CommentRequest.Post commentWriteDto);

    void deleteComment(Long commentId);

    void deleteCommentByUsername(Long commentId, String username);

    void deleteCommentForHardReset(Long commentId);

}

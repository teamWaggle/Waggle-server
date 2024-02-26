package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.web.dto.comment.CommentRequest.CommentCreateDto;

public interface CommentCommandService {
    Long createComment(Long boardId, CommentCreateDto commentWriteDto);

    Long createCommentByUsername(Long boardId, CommentCreateDto commentWriteDto, String username);

    Long updateComment(Long commentId, CommentCreateDto commentWriteDto);

    Long updateCommentByUsername(Long commentId, String username, CommentCreateDto commentWriteDto);

    void deleteComment(Long commentId);

    void deleteCommentByUsername(Long commentId, String username);

    void deleteCommentForHardReset(Long commentId);

}

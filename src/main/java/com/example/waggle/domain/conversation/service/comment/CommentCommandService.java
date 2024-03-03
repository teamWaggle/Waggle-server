package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.comment.CommentRequest;

public interface CommentCommandService {
    Long createComment(Long boardId, CommentRequest.Post request);

    Long createComment(Long boardId, Member member, CommentRequest.Post request);

    Long updateComment(Long commentId, CommentRequest.Post request);

    Long updateComment(Long commentId, Member member, CommentRequest.Post request);

    void deleteComment(Long commentId);

    void deleteComment(Long commentId, Member member);

    void deleteCommentForHardReset(Long commentId);

}

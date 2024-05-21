package com.example.waggle.domain.conversation.application.comment;

import com.example.waggle.domain.conversation.presentation.dto.comment.CommentRequest;
import com.example.waggle.domain.member.persistence.entity.Member;

public interface CommentCommandService {

    Long createComment(Long boardId, CommentRequest createCommentRequest, Member member);

    Long updateComment(Long commentId, CommentRequest updateCommentRequest, Member member);

    void deleteComment(Long commentId, Member member);

    void deleteCommentForHardReset(Long commentId);     //TODO remove

    void deleteCommentByAdmin(Long commentId, Member member);

}

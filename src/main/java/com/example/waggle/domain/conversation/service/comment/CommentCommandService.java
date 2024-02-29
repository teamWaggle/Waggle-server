package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.comment.CommentRequest;

public interface CommentCommandService {

    Long createComment(Long boardId, CommentRequest createCommentRequest, Member member);

    Long updateComment(Long commentId, CommentRequest updateCommentRequest, Member member);

    void deleteComment(Long commentId, Member member);

    void deleteCommentForHardReset(Long commentId);

}

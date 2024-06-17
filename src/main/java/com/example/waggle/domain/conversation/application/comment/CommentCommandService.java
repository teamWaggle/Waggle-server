package com.example.waggle.domain.conversation.application.comment;

import com.example.waggle.domain.conversation.presentation.dto.ConversationRequest;
import com.example.waggle.domain.member.persistence.entity.Member;

public interface CommentCommandService {

    Long createComment(Long boardId, ConversationRequest createCommentRequest, Member member);

    Long updateComment(Long commentId, ConversationRequest updateCommentRequest, Member member);

    void deleteComment(Long commentId, Member member);

    void deleteCommentByAdmin(Long commentId, Member member);

}

package com.example.waggle.domain.conversation.persistence.dao.comment.querydsl;

import com.example.waggle.domain.conversation.presentation.dto.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.domain.conversation.presentation.dto.CommentResponse.SirenCommentViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryRepository {

    Page<SirenCommentViewDto> findPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable);

    Page<QuestionCommentViewDto> findPagedQuestionCommentsByUserUrl(String userUrl, Pageable pageable);

    void deleteCommentsWithRelationsByBoard(Long boardId);

    void deleteCommentsWithRelations(Long commentId);

}

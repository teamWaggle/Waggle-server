package com.example.waggle.domain.conversation.application.comment;

import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.conversation.presentation.dto.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.domain.conversation.presentation.dto.CommentResponse.SirenCommentViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryService {
    Page<Comment> getPagedComments(Long boardId, Pageable pageable);

    Page<SirenCommentViewDto> getPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable);

    Page<QuestionCommentViewDto> getPagedQuestionCommentsByUserUrl(String userUrl, Pageable pageable);

}

package com.example.waggle.domain.conversation.repository;

import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryRepository {

    Page<SirenCommentViewDto> findPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable);

    Page<QuestionCommentViewDto> findPagedQuestionCommentsByUserUrl(String userUrl, Pageable pageable);

}

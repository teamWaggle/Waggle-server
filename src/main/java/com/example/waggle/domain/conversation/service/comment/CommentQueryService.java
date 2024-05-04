package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentViewDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryService {
    List<Comment> getComments(Long boardId);

    Page<Comment> getPagedComments(Long boardId, Pageable pageable);

    Page<SirenCommentViewDto> getPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable);

    Page<QuestionCommentViewDto> getPagedQuestionCommentsByUserUrl(String userUrl, Pageable pageable);

}

package com.example.waggle.domain.conversation.application.comment;

import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.conversation.presentation.dto.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.domain.conversation.presentation.dto.CommentResponse.SirenCommentViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentQueryServiceImpl implements CommentQueryService {
    private final CommentRepository commentRepository;

    @Override
    public Page<Comment> getPagedComments(Long boardId, Pageable pageable) {
        return commentRepository.findPagedCommentsByBoardId(boardId, pageable);
    }

    @Override
    public Page<SirenCommentViewDto> getPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable) {
        return commentRepository.findPagedSirenCommentsByUserUrl(userUrl, pageable);
    }

    @Override
    public Page<QuestionCommentViewDto> getPagedQuestionCommentsByUserUrl(String userUrl, Pageable pageable) {
        return commentRepository.findPagedQuestionCommentsByUserUrl(userUrl, pageable);
    }

}

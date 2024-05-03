package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.repository.CommentQueryRepository;
import com.example.waggle.domain.conversation.repository.CommentRepository;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentViewDto;
import java.util.List;
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
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public List<Comment> getComments(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    @Override
    public Page<Comment> getPagedComments(Long boardId, Pageable pageable) {
        return commentRepository.findPagedCommentsByBoardId(boardId, pageable);
    }

    @Override
    public Page<SirenCommentViewDto> getPagedSirenCommentsByUserUrl(String userUrl, Pageable pageable) {
        return commentQueryRepository.findPagedSirenCommentsByUserUrl(userUrl, pageable);
    }

    @Override
    public Page<QuestionCommentViewDto> getPagedQuestionCommentsByUserUrl(String userUrl, Pageable pageable) {
        return commentQueryRepository.findPagedQuestionCommentsByUserUrl(userUrl, pageable);
    }

}

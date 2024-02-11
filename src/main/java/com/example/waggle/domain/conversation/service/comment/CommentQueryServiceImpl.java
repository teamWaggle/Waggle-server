package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentQueryServiceImpl implements CommentQueryService {
    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getComments(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    @Override
    public Page<Comment> getPagedComments(Long boardId, Pageable pageable) {
        return commentRepository.findPagedCommentsByBoardId(boardId, pageable);
    }

}

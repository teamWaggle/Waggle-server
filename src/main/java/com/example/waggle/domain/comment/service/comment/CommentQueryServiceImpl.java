package com.example.waggle.domain.comment.service.comment;

import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.handler.CommentHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.UtilService;
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
public class CommentQueryServiceImpl implements CommentQueryService{
    private final CommentRepository commentRepository;
    private final UtilService utilService;
    @Override
    public List<Comment> getComments(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    @Override
    public Page<Comment> getPagedComments(Long boardId, Pageable pageable) {
        return commentRepository.findPagedCommentsByBoardId(boardId, pageable);
    }

    @Override
    public boolean validateMember(Long commentId) {
        Member signInMember = utilService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        return comment.getMember().equals(signInMember);
    }


}

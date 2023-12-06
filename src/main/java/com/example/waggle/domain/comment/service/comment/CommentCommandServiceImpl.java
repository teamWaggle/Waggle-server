package com.example.waggle.domain.comment.service.comment;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.handler.CommentHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.comment.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentCommandServiceImpl implements CommentCommandService{

    private final CommentRepository commentRepository;
    private final UtilService utilService;
    @Override
    public Long createComment(Long boardId, CommentRequest.Post commentWriteDto, BoardType boardType) {
        Member signInMember = utilService.getSignInMember();
        Board board = utilService.getBoard(boardId, boardType);
        Comment build = Comment.builder()
                .content(commentWriteDto.getContent())
                .board(board)
                .member(signInMember)
                .build();
        return build.getId();
    }

    @Override
    public Long updateComment(Long commentId, CommentRequest.Post commentWriteDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        comment.changeContent(commentWriteDto.getContent());
        return comment.getId();
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        if (!validateMember(commentId)) {
            throw new CommentHandler(ErrorStatus.COMMENT_CANNOT_EDIT_OTHERS);
        }
        commentRepository.delete(comment);
    }

    public boolean validateMember(Long commentId) {
        Member signInMember = utilService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        return comment.getMember().equals(signInMember);
    }
}

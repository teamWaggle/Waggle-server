package com.example.waggle.domain.comment.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.comment.CommentWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waggle.global.exception.ErrorCode.CANNOT_TOUCH_NOT_YOURS;
import static com.example.waggle.global.exception.ErrorCode.COMMENT_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentCommandServiceImpl implements CommentCommandService{

    private final CommentRepository commentRepository;
    private final UtilService utilService;
    @Override
    public Long createComment(Long boardId, CommentWriteDto commentWriteDto, BoardType boardType) {
        Member signInMember = utilService.getSignInMember();
        Board board = utilService.getBoard(boardId, boardType);
        Comment comment = commentRepository.save(commentWriteDto.toEntity(signInMember, board));
        return comment.getId();
    }

    @Transactional
    @Override
    public Long updateComment(Long commentId, CommentWriteDto commentWriteDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));
        comment.changeContent(commentWriteDto.getContent());
        return comment.getId();
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));
        if (!validateMember(commentId)) {
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }
        commentRepository.delete(comment);
    }
}

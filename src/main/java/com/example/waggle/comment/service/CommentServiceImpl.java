package com.example.waggle.comment.service;

import com.example.waggle.board.Board;
import com.example.waggle.comment.domain.Comment;
import com.example.waggle.comment.dto.CommentViewDto;
import com.example.waggle.comment.dto.CommentWriteDto;
import com.example.waggle.comment.repository.CommentRepository;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.CANNOT_TOUCH_NOT_YOURS;
import static com.example.waggle.commons.exception.ErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UtilService utilService;

    @Override
    public List<CommentViewDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream().map(CommentViewDto::toDto).collect(Collectors.toList());
    }

    @Transactional
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

    @Override
    public boolean validateMember(Long commentId) {
        Member signInMember = utilService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));
        return comment.getMember().equals(signInMember);
    }
}

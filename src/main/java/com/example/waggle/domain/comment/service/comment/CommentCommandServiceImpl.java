package com.example.waggle.domain.comment.service.comment;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.exception.handler.CommentHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.comment.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberQueryService memberQueryService;
    private final BoardService boardService;

    @Override
    public Long createComment(Long boardId, CommentRequest.Post commentWriteDto, BoardType boardType) {
        Member signInMember = memberQueryService.getSignInMember();
        Board board = boardService.getBoard(boardId, boardType);
        Comment build = Comment.builder()
                .content(commentWriteDto.getContent())
                .board(board)
                .member(signInMember)
                .build();
        commentRepository.save(build);
        return build.getId();
    }

    @Override
    public Long createCommentByUsername(Long boardId, CommentRequest.Post commentWriteDto, String username, BoardType boardType) {
        Member memberByUsername = memberQueryService.getMemberByUsername(username);
        Board board = boardService.getBoard(boardId, boardType);
        Comment build = Comment.builder()
                .content(commentWriteDto.getContent())
                .board(board)
                .member(memberByUsername)
                .build();
        commentRepository.save(build);
        return build.getId();
    }

    @Override
    public Long updateComment(Long commentId, CommentRequest.Post commentWriteDto) {
        if (!validateMember(commentId)) {
            throw new CommentHandler(ErrorStatus.COMMENT_CANNOT_EDIT_OTHERS);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        comment.changeContent(commentWriteDto.getContent());
        return comment.getId();
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!validateMember(commentId)) {
            throw new CommentHandler(ErrorStatus.COMMENT_CANNOT_EDIT_OTHERS);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        replyRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(comment);
    }

    public boolean validateMember(Long commentId) {
        Member signInMember = memberQueryService.getSignInMember();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        return comment.getMember().equals(signInMember);
    }
}

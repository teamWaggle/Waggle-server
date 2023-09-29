package com.example.waggle.comment.service;

import com.example.waggle.board.Board;
import com.example.waggle.comment.domain.Comment;
import com.example.waggle.member.domain.Member;
import com.example.waggle.comment.dto.CommentViewDto;
import com.example.waggle.comment.dto.CommentWriteDto;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.comment.repository.CommentRepository;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.commons.util.service.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UtilService utilService;

    //1. 조회
    //p1. 나중에 생성 역순으로 나열해야함
    public List<CommentViewDto> findComments(Long boardId) {
        List<Comment> commentsByBoardId = commentRepository.findByBoardId(boardId);
        return commentsByBoardId.stream().map(CommentViewDto::toDto).collect(Collectors.toList());
    }

    //2. 저장
    public Long saveComment(Long boardId, CommentWriteDto writeDto, BoardType boardType) {
        Member signInMember = utilService.getSignInMember();
        Board board = utilService.getBoard(boardId, boardType);
        Comment saveComment = writeDto.toEntity(signInMember, board);
        Comment save = commentRepository.save(saveComment);
        return save.getId();
    }

    //3. 수정
    // question
    public Long editComment(CommentViewDto viewDto, CommentWriteDto writeDto) {
        //check exist comment
        Comment comment = commentRepository.findById(viewDto.getId())
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));

        //edit
        comment.changeContent(writeDto.getContent());

        return comment.getId();
    }


    //3.1 check member
    public boolean checkMember(CommentViewDto viewDto) {
        Member signInMember = utilService.getSignInMember();
        Comment comment = commentRepository.findById(viewDto.getId())
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));
        return comment.getMember().equals(signInMember);
    }

    // 4. 삭제
    // 아래에서 따로 멤버 검증이 없는 이유는
    // 어차피 레포지토리에서 데이터를 가져올 때 멤버를 필터로 사용하기 때문이다.
    public void deleteComment(CommentViewDto viewDto) {
        Member signInMember = utilService.getSignInMember();
        //check exist comment
        Comment comment = commentRepository.findById(viewDto.getId())
                .orElseThrow(() -> new CustomPageException(COMMENT_NOT_FOUND));
        if (comment.getMember().equals(signInMember)) {
            log.info("delete completely!");
            commentRepository.delete(comment);
        }
    }
}

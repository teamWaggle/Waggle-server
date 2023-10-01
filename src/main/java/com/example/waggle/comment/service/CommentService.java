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

public interface CommentService {
    public List<CommentViewDto> findComments(Long boardId);

    public Long saveComment(Long boardId, CommentWriteDto writeDto, BoardType boardType);

    public Long editComment(CommentViewDto viewDto, CommentWriteDto writeDto);

    public boolean checkMember(CommentViewDto viewDto);

    public void deleteComment(CommentViewDto viewDto);
}

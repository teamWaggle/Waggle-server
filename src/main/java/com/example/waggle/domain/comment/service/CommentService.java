package com.example.waggle.domain.comment.service;

import com.example.waggle.domain.comment.dto.CommentViewDto;
import com.example.waggle.domain.comment.dto.CommentWriteDto;
import com.example.waggle.global.util.service.BoardType;

import java.util.List;

public interface CommentService {
    List<CommentViewDto> getComments(Long boardId);

    Long createComment(Long boardId, CommentWriteDto commentWriteDto, BoardType boardType);

    Long updateComment(Long commentId, CommentWriteDto commentWriteDto);

    void deleteComment(Long commentId);

    boolean validateMember(Long commentId);
}

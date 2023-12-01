package com.example.waggle.domain.comment.service;

import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.web.dto.comment.CommentWriteDto;

public interface CommentCommandService {
    Long createComment(Long boardId, CommentWriteDto commentWriteDto, BoardType boardType);

    Long updateComment(Long commentId, CommentWriteDto commentWriteDto);

    void deleteComment(Long commentId);

}

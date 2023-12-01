package com.example.waggle.domain.comment.service.comment;

import com.example.waggle.web.dto.comment.CommentViewDto;

import java.util.List;

public interface CommentQueryService {
    List<CommentViewDto> getComments(Long boardId);

    boolean validateMember(Long commentId);
}

package com.example.waggle.web.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommentRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CommentCreateDto {
        private String content;
        List<String> mentionedMemberList;
    }

}

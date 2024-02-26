package com.example.waggle.web.dto.comment;

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
    public static class CommentCreateDto {
        private String content;
        List<String> mentionedMemberList;
    }

}

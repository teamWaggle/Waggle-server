package com.example.waggle.web.dto.reply;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReplyRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ReplyCreateDto {
        private Long commentId;
        private String content;
        private List<String> mentionedMemberList;
    }

}
package com.example.waggle.web.dto.media;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MediaRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MediaCreateDto {
        public String imageUrl;
        public boolean allowUpload;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MediaDeleteDto {
        public String imageUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MediaUpdateDto {
        public Long boardId;
        public List<MediaCreateDto> mediaList;
        public List<MediaDeleteDto> deleteMediaList;
    }

}

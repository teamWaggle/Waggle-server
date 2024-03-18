package com.example.waggle.web.dto.media;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class MediaRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MediaListDto {
        public List<String> mediaList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MediaSingleDto {
        public String media;
    }


}

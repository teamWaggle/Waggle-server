package com.example.waggle.web.dto.media;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class MediaRequest {
    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDto {
        public String imageUrl;
        //0~4(maxSize=5)
        public boolean allowUpload;
    }

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteDto {
        public String imageUrl;
    }

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Put {
        public List<SaveDto> mediaList = new ArrayList<>();
        public List<DeleteDto> deleteMediaList = new ArrayList<>();
        public Long boardId;
    }

}

package com.example.waggle.web.converter;

import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.web.dto.media.MediaRequest;

public class MediaConverter {

    public static MediaRequest.SaveDto toPutDto(Media media) {
        return MediaRequest.SaveDto.builder()
                .id(media.getId())
                .imageUrl(media.getUploadFile())
                .build();
    }

}

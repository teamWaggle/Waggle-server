package com.example.waggle.web.converter;

import com.example.waggle.web.dto.media.MediaResponse.MediaListDto;
import com.example.waggle.web.dto.media.MediaResponse.MediaViewDto;

import java.util.List;
import java.util.stream.Collectors;

public class MediaConverter {
    public static MediaViewDto toMediaViewDto(String imgUrl) {
        return MediaViewDto.builder()
                .imgUrl(imgUrl)
                .build();
    }

    public static MediaListDto toMediaListDto(List<String> imgUrlList) {
        List<MediaViewDto> collect = imgUrlList.stream()
                .map(MediaConverter::toMediaViewDto).collect(Collectors.toList());
        return MediaListDto.builder()
                .mediaList(collect)
                .build();
    }
}

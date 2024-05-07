package com.example.waggle.domain.media.presentation.converter;

import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.domain.media.presentation.dto.MediaResponse.MediaListDto;
import com.example.waggle.domain.media.presentation.dto.MediaResponse.MediaViewDto;

import java.util.List;
import java.util.stream.Collectors;

public class MediaConverter {
    public static MediaViewDto toMediaViewDto(String imgUrl) {
        return MediaViewDto.builder()
                .imgUrl(MediaUtil.appendUri(imgUrl))
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

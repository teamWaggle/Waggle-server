package com.example.waggle.web.converter;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.media.MediaRequest;

import java.util.List;
import java.util.stream.Collectors;

public class MediaConverter {

    public static MediaRequest.SaveDto toPutDto(Media media) {
        return MediaRequest.SaveDto.builder()
                .id(media.getId())
                .imageUrl(media.getUploadFile())
                .build();
    }



}

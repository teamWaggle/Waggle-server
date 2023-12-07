package com.example.waggle.domain.media.service;

import com.example.waggle.domain.media.entity.Media;

import java.util.List;

public interface MediaQueryService {
    List<Media> findMediaList(Long boardId);
}

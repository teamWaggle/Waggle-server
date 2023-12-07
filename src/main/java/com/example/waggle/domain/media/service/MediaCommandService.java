package com.example.waggle.domain.media.service;

import com.example.waggle.global.util.service.BoardType;

import java.util.List;

public interface MediaCommandService {
    void createMedia(List<String> uploadFiles, Long boardId, BoardType boardtype);
    void updateMedia(List<String> uploadFiles, Long boardId, BoardType boardType);
}

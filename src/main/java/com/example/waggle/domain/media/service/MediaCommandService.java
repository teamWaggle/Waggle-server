package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;

import java.util.List;

public interface MediaCommandService {
    void createMedia(List<String> uploadFiles, Board board);
    void updateMedia(List<String> uploadFiles, Board board);
}

package com.example.waggle.domain.media.application;

import com.example.waggle.domain.board.persistence.entity.Board;

import java.util.List;

public interface MediaCommandService {

    void createMedia(List<String> imgUrlList, Board board);

    void updateMedia(List<String> imgUrlList, Board board);

    void deleteMedia(Board board);

}

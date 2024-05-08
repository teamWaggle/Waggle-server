package com.example.waggle.domain.media.persistence.dao;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.media.persistence.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByBoardId(Long boardId);

    Optional<Media> findByUploadFile(String uploadFile);

    void deleteMediaByBoardId(Long boardId);

    void deleteAllByBoard(Board board);

}
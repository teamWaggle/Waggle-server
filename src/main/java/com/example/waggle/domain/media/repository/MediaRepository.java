package com.example.waggle.domain.media.repository;

import com.example.waggle.domain.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByBoardId(Long boardId);
}
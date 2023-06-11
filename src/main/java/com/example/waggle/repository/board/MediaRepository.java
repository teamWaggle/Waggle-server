package com.example.waggle.repository.board;

import com.example.waggle.domain.board.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
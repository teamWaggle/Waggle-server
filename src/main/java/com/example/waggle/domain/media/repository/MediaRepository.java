package com.example.waggle.domain.media.repository;

import com.example.waggle.domain.media.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
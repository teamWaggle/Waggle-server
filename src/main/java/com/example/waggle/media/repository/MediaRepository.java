package com.example.waggle.media.repository;

import com.example.waggle.media.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
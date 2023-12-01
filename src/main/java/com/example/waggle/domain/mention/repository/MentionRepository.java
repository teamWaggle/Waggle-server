package com.example.waggle.domain.mention.repository;

import com.example.waggle.domain.mention.entity.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentionRepository extends JpaRepository<Mention, Long> {
}
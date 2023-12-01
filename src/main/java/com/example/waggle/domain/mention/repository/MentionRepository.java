package com.example.waggle.domain.mention.repository;

import com.example.waggle.domain.mention.domain.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentionRepository extends JpaRepository<Mention, Long> {
}
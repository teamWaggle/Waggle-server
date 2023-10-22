package com.example.waggle.mention.repository;

import com.example.waggle.mention.domain.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentionRepository extends JpaRepository<Mention, Long> {
}
package com.example.waggle.memberMention.repository;

import com.example.waggle.memberMention.domain.MemberMention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMentionRepository extends JpaRepository<MemberMention, Long> {
}
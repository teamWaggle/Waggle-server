package com.example.waggle.repository.board.comment;

import com.example.waggle.domain.board.comment.MemberMention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMentionRepository extends JpaRepository<MemberMention, Long> {
}
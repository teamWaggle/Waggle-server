package com.example.waggle.domain.follow.repository;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFromUser_Username(String username);

    List<Follow> findByToUser_Username(String username);

    boolean existsFollowByFromUserAndToUser(Member fromUser, Member toUser);
}

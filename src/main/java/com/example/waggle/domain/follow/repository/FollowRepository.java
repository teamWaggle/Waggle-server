package com.example.waggle.domain.follow.repository;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFromUser_Username(String username);

    List<Follow> findByToUser_Username(String username);

    Optional<Follow> findByToUser_UsernameAndFromUser_Username(String toUser_username, String fromUser_username);

    boolean existsFollowByFromUserAndToUser(Member fromUser, Member toUser);
}

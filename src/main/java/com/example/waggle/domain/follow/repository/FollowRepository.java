package com.example.waggle.domain.follow.repository;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFromMember_Username(String username);

    List<Follow> findByFromMember_UserUrl(String userUrl);

    List<Follow> findByFromMemberId(Long fromMemberId);

    List<Follow> findByToMember_Username(String username);

    List<Follow> findByToMember_UserUrl(String userUrl);

    List<Follow> findByToMemberId(Long memberId);

    Optional<Follow> findByToMemberAndFromMember(Member to, Member from);

    boolean existsFollowByFromMemberAndToMember(Member fromUser, Member toUser);

    void deleteAllByFromMemberUsername(String fromMemberUsername);

    void deleteAllByToMemberUsername(String toMemberUsername);

    void deleteAllByToMember(Member member);

    void deleteAllByFromMember(Member member);

}

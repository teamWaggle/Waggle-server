package com.example.waggle.domain.follow.application;

import com.example.waggle.domain.follow.persistence.entity.Follow;
import com.example.waggle.domain.member.persistence.entity.Member;

import java.util.List;

public interface FollowQueryService {
    List<Follow> getFollowings(Long memberId);

    List<Follow> getFollowingsByUsername(String username);

    List<Follow> getFollowingsByUserUrl(String userUrl);

    List<Follow> getFollowers(Long memberId);

    List<Follow> getFollowersByUsername(String username);

    List<Follow> getFollowersByUserUrl(String userUrl);

    Boolean checkFollowing(Member member, String userUrl);
}

package com.example.waggle.domain.follow.application;

import com.example.waggle.domain.follow.persistence.entity.Follow;
import com.example.waggle.domain.member.persistence.entity.Member;

import java.util.List;

public interface FollowQueryService {

    List<Follow> getFollowingsByUserUrl(String userUrl);

    List<Follow> getFollowersByUserUrl(String userUrl);

    Boolean isFollowingMemberWithUserUrl(Member member, String userUrl);
}

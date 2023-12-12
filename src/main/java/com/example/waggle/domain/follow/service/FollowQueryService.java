package com.example.waggle.domain.follow.service;

import com.example.waggle.domain.follow.entity.Follow;

import java.util.List;

public interface FollowQueryService {
    List<Follow> getFollowings(String username);

    List<Follow> getFollowers(String username);
}

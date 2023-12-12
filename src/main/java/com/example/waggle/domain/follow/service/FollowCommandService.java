package com.example.waggle.domain.follow.service;

public interface FollowCommandService {

    Long follow(String username);

    void unFollow(Long followId);

}

package com.example.waggle.domain.follow.service;

public interface FollowCommandService {

    Long follow(String from, String to);

    void unFollow(String from, String to);

}

package com.example.waggle.domain.follow.application;

import com.example.waggle.domain.member.persistence.entity.Member;

public interface FollowCommandService {

    Long follow(String from, String to);

    Long follow(Member from, Long to);

    void unFollow(String from, String to);

    void unFollow(Member from, Long to);

}

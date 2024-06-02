package com.example.waggle.domain.follow.application;

import com.example.waggle.domain.member.persistence.entity.Member;

public interface FollowCommandService {

    Long follow(Member from, String to);

    void unFollow(Member from, String to);

}

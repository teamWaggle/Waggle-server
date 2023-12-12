package com.example.waggle.domain.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowQueryServiceImpl implements FollowQueryService{

    private final MemberQueryService memberQueryService;

    @Override
    public List<Follow> getFollowings(String username) {
        return memberQueryService.getMemberByUsername(username).getFollowingList();
    }

    @Override
    public List<Follow> getFollowers(String username) {
        return memberQueryService.getMemberByUsername(username).getFollowerList();
    }

    @Override
    public List<Follow> getFollowingsByUser() {
        Member member = memberQueryService.getSignInMember();
        return member.getFollowingList();
    }

    @Override
    public List<Follow> getFollowersByUser() {
        Member member = memberQueryService.getSignInMember();
        return member.getFollowerList();
    }
}

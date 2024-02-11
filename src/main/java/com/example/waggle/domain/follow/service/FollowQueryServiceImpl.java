package com.example.waggle.domain.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowQueryServiceImpl implements FollowQueryService {

    private final FollowRepository followRepository;

    @Override
    public List<Follow> getFollowings(Long memberId) {
        return followRepository.findByFromMemberId(memberId);
    }

    @Override
    public List<Follow> getFollowingsByUsername(String username) {
        return followRepository.findByFromMember_Username(username);
    }

    @Override
    public List<Follow> getFollowers(Long memberId) {
        return followRepository.findByToMemberId(memberId);
    }

    @Override
    public List<Follow> getFollowersByUsername(String username) {
        return followRepository.findByToMember_Username(username);
    }

}

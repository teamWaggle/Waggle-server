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
public class FollowQueryServiceImpl implements FollowQueryService{

    private final FollowRepository followRepository;

    @Override
    public List<Follow> getFollowings(String username) {
        return followRepository.findByFromUser_Username(username);
    }

    @Override
    public List<Follow> getFollowers(String username) {
        return followRepository.findByToUser_Username(username);
    }

}

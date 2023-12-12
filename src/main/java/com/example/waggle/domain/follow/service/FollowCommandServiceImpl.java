package com.example.waggle.domain.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.repository.FollowRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.exception.handler.FollowHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FollowCommandServiceImpl implements FollowCommandService{
    private final FollowRepository followRepository;
    private final MemberQueryService memberQueryService;
    @Override
    public Long follow(String username) {
        Member signInMember = memberQueryService.getSignInMember();
        Member followee = memberQueryService.getMemberByUsername(username);
        validateFollowing(signInMember, followee);
        Follow follow = Follow.builder()
                .fromMember(signInMember)
                .toMember(followee)
                .build();
        followRepository.save(follow);
        return follow.getId();
    }

    @Override
    public void unFollow(String username) {
        Follow follow = followRepository
                .findByToUser_UsernameAndFromUser_Username(username, SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);
    }
    private void validateFollowing(Member member, Member followee) {
        boolean isFollowExists = followRepository.existsFollowByFromUserAndToUser(member, followee);
        if (isFollowExists) {
            throw new FollowHandler(ErrorStatus.FOLLOW_ALREADY_EXIST);
        }
    }
}

package com.example.waggle.domain.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.repository.FollowRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.exception.handler.FollowHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
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
        Member user = memberQueryService.getSignInMember();
        Member follower = memberQueryService.getMemberByUsername(username);
        Follow follow = Follow.builder()
                .fromUser(user)
                .toUser(follower)
                .build();
        followRepository.save(follow);
        return follow.getId();
    }

    @Override
    public void unFollow(Long followId) {
        Member member = memberQueryService.getSignInMember();
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND));
        validateFollowing(member, follow);
        followRepository.delete(follow);
    }

    private static void validateFollowing(Member member, Follow follow) {
        if (!follow.getFromUser().equals(member)) {
            throw new FollowHandler(ErrorStatus.FOLLOW_NOT_AUTHENTICATED_UNFOLLOW);
        }
    }
}

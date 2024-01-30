package com.example.waggle.domain.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.repository.FollowRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.exception.handler.FollowHandler;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FollowCommandServiceImpl implements FollowCommandService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final MemberQueryService memberQueryService;

    @Override
    public Long follow(String from, String to) {
        Member member = memberRepository.findByUsername(from)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member followee = memberRepository.findByUsername(to)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        validateFollowing(member, followee);
        Follow follow = Follow.builder()
                .fromMember(member)
                .toMember(followee)
                .build();
        followRepository.save(follow);
        return follow.getId();
    }

    @Override
    public void unFollow(String from, String to) {
        Follow follow = followRepository
                .findByToMember_UsernameAndFromMember_Username(to, from)
                .orElseThrow(() -> new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);
    }

    private void validateFollowing(Member member, Member followee) {
        boolean isFollowExists = followRepository.existsFollowByFromMemberAndToMember(member, followee);
        if (isFollowExists) {
            throw new FollowHandler(ErrorStatus.FOLLOW_ALREADY_EXIST);
        }
    }
}

package com.example.waggle.domain.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.repository.FollowRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.domain.notification.repository.NotificationRepository;
import com.example.waggle.global.exception.handler.FollowHandler;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.NotificationConverter;
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
    private final NotificationRepository notificationRepository;

    @Override
    public Long follow(String from, String to) {
        Member member = memberRepository.findByUsername(from)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member followee = memberRepository.findByNickname(to)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        validateFollowing(member, followee);
        Follow follow = buildFollow(member, followee);
        followRepository.save(follow);
        return follow.getId();
    }

    @Override
    public Long follow(Member from, Long to) {
        Member followee = memberRepository.findById(to)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        validateFollowing(from, followee);
        Follow follow = buildFollow(from, followee);
        followRepository.save(follow);
        notificationRepository.save(
                Notification.of(from, NotificationConverter.toRequest(follow, to))
        );
        return follow.getId();
    }

    @Override
    public void unFollow(String from, String to) {
        Member fromMember = memberRepository.findByUsername(from)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member toMember = memberRepository.findByNickname(to)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Follow follow = followRepository
                .findByToMemberAndFromMember(toMember, fromMember)
                .orElseThrow(() -> new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);
    }

    @Override
    public void unFollow(Member from, Long to) {
        Member followee = memberRepository.findById(to)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Follow follow = followRepository.findByToMemberAndFromMember(followee, from)
                .orElseThrow(() -> new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);
    }

    private void validateFollowing(Member member, Member followee) {
        boolean isFollowExists = followRepository.existsFollowByFromMemberAndToMember(member, followee);
        if (isFollowExists) {
            throw new FollowHandler(ErrorStatus.FOLLOW_ALREADY_EXIST);
        }
        if (member == followee) {
            throw new FollowHandler(ErrorStatus.FOLLOW_NOT_ALLOWED_MYSELF);
        }
    }

    private static Follow buildFollow(Member member, Member followee) {
        Follow follow = Follow.builder()
                .fromMember(member)
                .toMember(followee)
                .build();
        return follow;
    }
}

package com.example.waggle.domain.follow.application;

import com.example.waggle.domain.follow.persistence.dao.FollowRepository;
import com.example.waggle.domain.follow.persistence.entity.Follow;
import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.notification.persistence.dao.NotificationRepository;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.persistence.entity.NotificationType;
import com.example.waggle.domain.notification.presentation.dto.NotificationRequest.FollowDto;
import com.example.waggle.exception.object.handler.FollowHandler;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.ObjectUtil;
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
    public Long follow(Member from, String to) {
        Member followee = memberRepository.findByUserUrl(to)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        validateFollowing(from, followee);
        Follow follow = buildFollow(from, followee);
        followRepository.save(follow);
        notificationRepository.save(buildNotification(follow, buildFollowDto(followee)));
        return follow.getId();
    }


    @Override
    public void unFollow(Member from, String to) {
        Member followee = memberRepository.findByUserUrl(to)
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
        return Follow.builder()
                .fromMember(member)
                .toMember(followee)
                .build();
    }

    private static FollowDto buildFollowDto(Member followee) {
        return FollowDto.builder().followeeNickname(followee.getNickname()).build();
    }

    private static Notification buildNotification(Follow follow, FollowDto content) {
        return Notification.builder()
                .sender(follow.getFromMember())
                .receiver(follow.getToMember())
                .isRead(false)
                .content(ObjectUtil.serialize(content))
                .type(NotificationType.FOLLOWED)
                .build();
    }
}

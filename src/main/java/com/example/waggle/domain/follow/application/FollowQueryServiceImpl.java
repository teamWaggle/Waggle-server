package com.example.waggle.domain.follow.application;

import com.example.waggle.domain.follow.persistence.dao.FollowRepository;
import com.example.waggle.domain.follow.persistence.entity.Follow;
import com.example.waggle.domain.member.persistence.entity.Member;
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
    public List<Follow> getFollowingsByUserUrl(String userUrl) {
        return followRepository.findByFromMember_UserUrl(userUrl);
    }

    @Override
    public List<Follow> getFollowersByUserUrl(String userUrl) {
        return followRepository.findByToMember_UserUrl(userUrl);
    }

    @Override
    public Boolean isFollowingMemberWithUserUrl(Member member, String userUrl) {
        return followRepository.existsFollowByFromMemberAndToMemberUserUrl(member, userUrl);
    }

}

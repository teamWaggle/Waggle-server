package com.example.waggle.domain.member.service;

import static com.example.waggle.global.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));
    }
}

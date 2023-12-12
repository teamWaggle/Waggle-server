package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
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
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public Member getSignInMember() {
        //check login
        if (isAuthenticated()) {
            //check exist user
            Member signInMember = getMemberByUsername(SecurityUtil.getCurrentUsername());
            return signInMember;
        }
        throw new MemberHandler(ErrorStatus.MEMBER_REFRESH_TOKEN_NOT_FOUND);
    }

    public boolean isAuthenticated() {
        if (SecurityUtil.getCurrentUsername().equals("anonymousUser")) {
            return false;
        }
        return true;
    }

    public void validateEmailDuplication(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_EMAIL);
        }
    }

    @Override
    public void validateUsernameDuplication(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_USERNAME);
        }
    }

    @Override
    public void validateNicknameDuplication(String nickname) {
        if (memberRepository.existsByUsername(nickname)) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_NICKNAME);
        }
    }
}

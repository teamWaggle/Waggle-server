package com.example.waggle.domain.member.application;

import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return member;
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
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

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public Member getMemberByUserUrl(String userUrl) {
        return memberRepository.findByUserUrl(userUrl)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public List<Member> getMembersByNameAndBirthday(String name, LocalDate birthday) {
        return memberRepository.findByNameAndBirthday(name, birthday);
    }

    @Override
    public List<Member> getMembersByNicknameContaining(String nickname) {
        return memberRepository.findByNicknameContaining(nickname);
    }

    @Override
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
    public void validateUserUrlDuplication(String userUrl) {
        if (memberRepository.existsByUserUrl(userUrl)) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_USER_URL);
        }
    }

    @Override
    public void validateNicknameDuplication(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_NICKNAME);
        }
    }
}

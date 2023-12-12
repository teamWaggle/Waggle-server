package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.member.MemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandServiceImpl implements MemberCommandService {

    private static final String DEFAULT_ROLE = "USER";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberQueryService memberQueryService;

    @Override
    public Long signUp(MemberRequest.RegisterRequestDto request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_USERNAME);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        List<String> roles = new ArrayList<>();
        roles.add(DEFAULT_ROLE);

        Member createdMember = Member.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .address(request.getAddress())
                .phone(request.getPhone())
                .roles(roles)
                .build();

        Member member = memberRepository.save(createdMember);

        return member.getId();
    }

    @Override
    public Long updateMemberInfo(MemberRequest.PutDto request) {
        Member member = memberQueryService.getSignInMember();
        member.updateInfo(request);
        return member.getId();
    }

    @Override
    public void deleteMember() {
        Member member = memberQueryService.getSignInMember();
        //TODO member relation all data removing
    }

}

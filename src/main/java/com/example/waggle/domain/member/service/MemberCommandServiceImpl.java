package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.VerifyMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberQueryService memberQueryService;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final RedisService redisService;



    @Override
    public Long signUp(MemberRequest.RegisterDto request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATE_USERNAME);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        Member createdMember = Member.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .email(request.getEmail())
                .address(request.getAddress())
                .phone(request.getPhone())
                .profileImgUrl(request.getProfileImg())
                .role(Role.USER)
                .build();

        Member member = memberRepository.save(createdMember);

        return member.getId();
    }



    @Override
    public Long updateMemberInfo(MemberRequest.Put request) {
        Member member = memberQueryService.getSignInMember();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        member.updateInfo(request, encodedPassword);
        return member.getId();
    }

    @Override
    public void deleteMember() {
        Member member = memberQueryService.getSignInMember();
        //TODO member relation all data removing
    }

    @Override
    public void verifyMail(VerifyMailRequest request) {
        String authNum = redisService.getValue(AUTH_CODE_PREFIX + request.getEmail());
        boolean isSuccess = authNum.equals(request.getAuthCode());
        if (!isSuccess) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);  // TODO 인증 실패 에러 코드
        }
    }

}

package com.example.waggle.service.member;

import com.example.waggle.component.ErrorCode;
import com.example.waggle.config.BusinessException;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.member.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public MemberDto signIn(SignInDto signInDto) {
        Member member = memberRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberDto.toDto(member); // TODO ResponseEntity.ok()
    }

//    public String createToken(Member member) {
//        Jwts.builder()
//                .signWith(key, SignatureAlgorithm.HS512)
//                .setHeaderParam(JWT_HEADER_PARAM_TYPE, headerType)
//                .setSubject(member.getId().toString())
//                .setIssuer(issuer)
//                .setExpiration(new Date((new Date()).getTime() + accessTime))
//                .setIssuedAt(new Date())
//                .compact();
//    }


    @Transactional
    @Override
    public MemberDto signUp(SignUpDto signUpDto) {
        return MemberDto.toDto(memberRepository.save(signUpDto.toEntity()));
    }

    @Override
    public void signOut(HttpSession session) {

    }
}

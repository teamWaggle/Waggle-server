package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.security.JwtToken;
import com.example.waggle.web.dto.member.MemberRequest;

public interface MemberCommandService {

    JwtToken signIn(MemberRequest.LoginRequestDto request);

    Member signUp(MemberRequest.RegisterRequestDto request, String profileImg);

    JwtToken refreshToken(String refreshToken);

}

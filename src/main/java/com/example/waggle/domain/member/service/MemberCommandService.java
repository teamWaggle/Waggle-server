package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.security.JwtToken;
import com.example.waggle.web.dto.member.MemberRequest;
import jakarta.servlet.http.HttpSession;

public interface MemberCommandService {

    JwtToken signIn(MemberRequest.LoginRequestDto request);

    Member signUp(MemberRequest.RegisterRequestDto request, String profileImg);

    void logout(HttpSession session);

    JwtToken refreshToken(String refreshToken);

}

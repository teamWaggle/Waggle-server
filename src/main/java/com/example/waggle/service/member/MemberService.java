package com.example.waggle.service.member;

import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.dto.member.*;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    JwtToken signIn(SignInDto signInDto);
    MemberDto signUp(SignUpDto signUpDto);
    void signOut(HttpSession session);

    MemberSimpleDto findMemberSimpleDto(String username);
}

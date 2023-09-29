package com.example.waggle.service.member;

import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    JwtToken signIn(SignInDto signInDto);
    MemberDto signUp(SignUpDto signUpDto);
    MemberDto signUpWithProfileImg(SignUpDto signUpDto, UploadFile profileImg);
    void signOut(HttpSession session);

    MemberSimpleDto findMemberSimpleDto(String username);
    MemberDto changeProfileImg(String username, UploadFile profileImg);
}

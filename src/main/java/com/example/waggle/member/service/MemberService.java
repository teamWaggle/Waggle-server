package com.example.waggle.member.service;

import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.security.JwtToken;
import com.example.waggle.member.dto.MemberDetailDto;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.dto.SignInDto;
import com.example.waggle.member.dto.SignUpDto;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    JwtToken signIn(SignInDto signInDto);

    MemberDetailDto signUp(SignUpDto signUpDto, UploadFile profileImg);

    void signOut(HttpSession session);

    MemberSummaryDto getMemberSummaryDto(String username);

    MemberDetailDto updateProfileImg(String username, UploadFile profileImg);
}

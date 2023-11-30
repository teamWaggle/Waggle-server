package com.example.waggle.domain.member.service;

import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.security.JwtToken;
import com.example.waggle.web.dto.member.MemberSummaryDto;
import com.example.waggle.web.dto.member.SignInDto;
import com.example.waggle.web.dto.member.SignUpDto;
import jakarta.servlet.http.HttpSession;

public interface MemberCommandService {

    JwtToken signIn(SignInDto signInDto);

    MemberSummaryDto signUp(SignUpDto signUpDto, UploadFile profileImg);

    void signOut(HttpSession session);

    Long updateProfileImg(String username, UploadFile profileImg);

}

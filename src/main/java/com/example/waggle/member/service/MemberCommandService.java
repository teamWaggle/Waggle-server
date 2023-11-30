package com.example.waggle.member.service;

import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.security.JwtToken;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.dto.SignInDto;
import com.example.waggle.member.dto.SignUpDto;
import jakarta.servlet.http.HttpSession;

public interface MemberCommandService {

    JwtToken signIn(SignInDto signInDto);

    MemberSummaryDto signUp(SignUpDto signUpDto, UploadFile profileImg);

    void signOut(HttpSession session);

    Long updateProfileImg(String username, UploadFile profileImg);

}

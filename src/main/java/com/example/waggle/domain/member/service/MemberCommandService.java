package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.VerifyMailRequest;

public interface MemberCommandService {

    Long signUp(MemberRequest.SignUpDto request);

    Long registerMemberInfo(String username, MemberRequest.RegisterDto request);

    Long updateMemberInfo(MemberRequest.Put request);

    void deleteMember();

    void verifyMail(VerifyMailRequest request);

}

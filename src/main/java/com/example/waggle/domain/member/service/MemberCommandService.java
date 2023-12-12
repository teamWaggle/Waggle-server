package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.VerifyMailRequest;

public interface MemberCommandService {

    Long signUp(MemberRequest.RegisterRequestDto request);

    Long updateMemberInfo(MemberRequest.PutDto request);

    void deleteMember();

    void verifyMail(VerifyMailRequest request);

}

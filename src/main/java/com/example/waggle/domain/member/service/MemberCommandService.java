package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.VerifyMailRequest;

public interface MemberCommandService {

    Long signUp(MemberRequest.AccessDto request);

    Long registerMemberInfo(String username, MemberRequest.RegisterDto request);

    Long updateMemberInfo(String username, MemberRequest.Put request);

    Long updatePassword(Long memberId, String password);

    Long verifyEmailForPasswordChange(VerifyMailRequest.AuthDto request);

    void deleteMember(Long memberId);

    void verifyMail(VerifyMailRequest.AuthDto request);

}

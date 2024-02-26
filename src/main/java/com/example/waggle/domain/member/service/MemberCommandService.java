package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.MemberRequest.MemberUpdateDto;
import com.example.waggle.web.dto.member.MemberRequest.TemporaryRegisterDto;
import com.example.waggle.web.dto.member.VerifyMailRequest.EmailVerificationDto;

public interface MemberCommandService {

    Long signUp(TemporaryRegisterDto request);

    Long registerMemberInfo(String username, MemberRequest.RegisterDto request);

    Long updateMemberInfo(String username, MemberUpdateDto request);

    Long updatePassword(Long memberId, String password);

    Long verifyEmailForPasswordChange(EmailVerificationDto request);

    void deleteMember(Long memberId);

    void verifyMail(EmailVerificationDto request);

}

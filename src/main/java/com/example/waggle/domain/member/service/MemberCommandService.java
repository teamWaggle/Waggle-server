package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberRequest.MemberProfileDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberUpdateDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.member.VerifyMailRequest.EmailVerificationDto;

public interface MemberCommandService {

    Long signUp(MemberCredentialsDto registerMemberRequest);

    Long initializeMemberProfile(String username, MemberProfileDto memberProfileRequest);

    Long updateMemberProfile(String username, MemberUpdateDto updateMemberRequest);

    Long updatePassword(Long memberId, String password);

    Long verifyEmailForPasswordChange(EmailVerificationDto verifyEmailRequest);

    void deleteMember(Long memberId);

    void verifyMail(EmailVerificationDto verifyEmailRequest);

}

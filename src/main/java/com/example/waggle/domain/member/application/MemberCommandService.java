package com.example.waggle.domain.member.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberCredentialsDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberProfileDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberUpdateDto;
import com.example.waggle.domain.member.presentation.dto.VerifyMailRequest.EmailVerificationDto;


public interface MemberCommandService {

    Long signUp(MemberCredentialsDto registerMemberRequest);

    Long initializeMemberProfile(MemberProfileDto memberProfileRequest,
                                 Member member);

    Long updateMemberProfile(MemberUpdateDto updateMemberRequest,
                             Member member);

    Long updatePassword(Long memberId, String password);

    Long convertRole(Member member, Role to);

    Long verifyEmailForPasswordChange(EmailVerificationDto verifyEmailRequest);

    void verifyMail(EmailVerificationDto verifyEmailRequest);

    void deleteMember(Long memberId);

    void deleteDormantMember();

    void deleteMemberByAdmin(Member member, Long memberId);

}

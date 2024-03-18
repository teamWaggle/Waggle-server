package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberProfileDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberUpdateDto;
import com.example.waggle.web.dto.member.VerifyMailRequest.EmailVerificationDto;

import static com.example.waggle.web.dto.media.MediaRequest.MediaSingleDto;


public interface MemberCommandService {

    Long signUp(MemberCredentialsDto registerMemberRequest);

    Long initializeMemberProfile(MemberProfileDto memberProfileRequest,
                                 MediaSingleDto memberProfileImg,
                                 Member member);

    Long updateMemberProfile(MemberUpdateDto updateMemberRequest,
                             MediaSingleDto memberProfileImg,
                             Member member);

    Long updatePassword(Long memberId, String password);

    Long convertRole(Member member, Role to);

    Long verifyEmailForPasswordChange(EmailVerificationDto verifyEmailRequest);

    void verifyMail(EmailVerificationDto verifyEmailRequest);

    void deleteMember(Long memberId);

    void deleteDormantMember();

    void deleteMemberAsAdmin(Member member, Long memberId);

}

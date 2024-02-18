package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;

import java.time.LocalDate;
import java.util.List;

public interface MemberQueryService {

    Member getMemberByUsername(String username);

    Member getMemberByEmail(String email);

    Member getSignInMember();

    Member getMemberById(Long memberId);

    List<Member> getMembersByNameAndBirthday(String name, LocalDate birthday);

    boolean isAuthenticated();

    void validateEmailDuplication(String email);

    void validateUserUrlDuplication(String userUrl);

    void validateNicknameDuplication(String nickname);

}
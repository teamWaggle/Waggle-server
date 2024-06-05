package com.example.waggle.domain.member.application;

import com.example.waggle.domain.member.persistence.entity.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberQueryService {

    Member getMemberByUsername(String username);

    Member getMemberByEmail(String email);

    Member getMemberById(Long memberId);

    Member getMemberByUserUrl(String userUrl);

    Optional<Member> findOptionalMemberByUserUrl(String userUrl);

    List<Member> getMembersByNameAndBirthday(String name, LocalDate birthday);

    List<Member> getMembersByNicknameContaining(String nickname);

    boolean isAuthenticated();

    void validateEmailDuplication(String email);

    void validateUserUrlDuplication(String userUrl);

    void validateNicknameDuplication(String nickname);

}
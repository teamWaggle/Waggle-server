package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;

public interface MemberQueryService {

    Member getMemberByUsername(String username);

    void validateEmailDuplication(String email);

    void validateUsernameDuplication(String username);

    void validateNicknameDuplication(String nickname);

}
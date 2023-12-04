package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;

public interface MemberQueryService {

    Member getMemberByUsername(String username);

}
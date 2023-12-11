package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberRequest;

public interface MemberCommandService {

    Long signUp(MemberRequest.RegisterRequestDto request);

    Long updateMemberInfo(MemberRequest.PutDto request);

    void deleteMember();

}

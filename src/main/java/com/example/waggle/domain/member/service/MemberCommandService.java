package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.member.MemberRequest;

public interface MemberCommandService {

    Member signUp(MemberRequest.RegisterRequestDto request, String profileImg);

}

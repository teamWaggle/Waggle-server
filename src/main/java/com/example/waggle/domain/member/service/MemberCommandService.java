package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.VerifyMailRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {

    Long signUp(MemberRequest.RegisterDto request);

    Long updateMemberInfo(MemberRequest.Put request);

    void deleteMember();

    void verifyMail(VerifyMailRequest request);

}

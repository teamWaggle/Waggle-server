package com.example.waggle.member.service;

import static com.example.waggle.commons.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.example.waggle.commons.exception.CustomApiException;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberQueryServiceImpl implements MemberQueryService{

    private final MemberRepository memberRepository;

    @Override
    public MemberSummaryDto getMemberSummaryDto(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException(MEMBER_NOT_FOUND));
        return MemberSummaryDto.toDto(member);
    }
}

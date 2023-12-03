package com.example.waggle.web.converter;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.member.MemberResponse;

public class MemberConverter {

    public static MemberResponse.MemberSummaryDto toMemberSummaryDto(Member member) {
        return MemberResponse.MemberSummaryDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImgUrl(member.getProfileImgUrl()).build();
    }

    public static MemberResponse.MemberDetailDto toMemberDetailDto(Member member) {
        return MemberResponse.MemberDetailDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .phone(member.getPhone())
                .profileImgUrl(member.getProfileImgUrl()).build();
    }

}

package com.example.waggle.web.converter;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.member.MemberResponse;

public class MemberConverter {

    public static MemberResponse.SummaryDto toMemberSummaryDto(Member member) {
        return MemberResponse.SummaryDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImgUrl(MediaUtil.getProfileImg(member)).build();
    }

    public static MemberResponse.DetailDto toMemberDetailDto(Member member) {
        return MemberResponse.DetailDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .phone(member.getPhone())
                .profileImgUrl(MediaUtil.getProfileImg(member)).build();
    }

}

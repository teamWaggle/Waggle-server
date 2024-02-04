package com.example.waggle.web.converter;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.member.MemberResponse;

public class MemberConverter {

    public static MemberResponse.SummaryDto toMemberSummaryDto(Member member) {
        return MemberResponse.SummaryDto.builder()
                .id(member.getId())
                .userUrl(member.getUsername())
                .nickname(member.getNickname())
                .profileImgUrl(MediaUtil.getProfileImg(member)).build();
    }

    public static MemberResponse.DetailDto toMemberDetailDto(Member member) {
        return MemberResponse.DetailDto.builder()
                .id(member.getId())
                .userUrl(member.getUsername())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .name(member.getName())
                .profileImgUrl(MediaUtil.getProfileImg(member)).build();
    }

}

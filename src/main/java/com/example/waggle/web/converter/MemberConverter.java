package com.example.waggle.web.converter;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.util.EmailUtil;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.member.MemberResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MemberConverter {

    public static MemberResponse.SummaryDto toMemberSummaryDto(Member member) {
        return MemberResponse.SummaryDto.builder()
                .id(member.getId())
                .userUrl(member.getUserUrl())
                .nickname(member.getNickname())
                .profileImgUrl(MediaUtil.getProfileImg(member)).build();
    }

    public static MemberResponse.DetailDto toMemberDetailDto(Member member) {
        return MemberResponse.DetailDto.builder()
                .id(member.getId())
                .userUrl(member.getUserUrl())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .name(member.getName())
                .profileImgUrl(MediaUtil.getProfileImg(member)).build();
    }

    public static MemberResponse.ListDto toMemberListDto(List<Member> memberList) {
        return MemberResponse.ListDto.builder()
                .members(memberList.stream()
                        .map(MemberConverter::toMemberSummaryDto).collect(Collectors.toList()))
                .totalSize(memberList.size())
                .build();

    }

    public static MemberResponse.EmailListDto toEmailListDto(List<Member> memberList) {
        return MemberResponse.EmailListDto.builder()
                .emails(memberList.stream()
                        .map(member -> EmailUtil.maskEmail(member.getEmail())).collect(Collectors.toList()))
                .build();
    }

}

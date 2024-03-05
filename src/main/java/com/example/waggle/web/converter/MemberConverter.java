package com.example.waggle.web.converter;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.util.EmailUtil;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.member.MemberResponse.*;

import java.util.List;
import java.util.stream.Collectors;

public class MemberConverter {

    public static MemberSummaryDto toMemberSummaryDto(Member member) {
        return MemberSummaryDto.builder()
                .memberId(member.getId())
                .userUrl(member.getUserUrl())
                .nickname(member.getNickname())
                .profileImgUrl(MediaUtil.getProfileImg(member))
                .build();
    }

    public static MemberDetailDto toMemberDetailDto(Member member) {
        return MemberDetailDto.builder()
                .memberId(member.getId())
                .userUrl(member.getUserUrl())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .name(member.getName())
                .profileImgUrl(MediaUtil.getProfileImg(member))
                .build();
    }

    public static MemberSummaryListDto toMemberListDto(List<Member> memberList) {
        return MemberSummaryListDto.builder()
                .memberList(memberList.stream()
                        .map(MemberConverter::toMemberSummaryDto).collect(Collectors.toList()))
                .memberCount(memberList.size())
                .build();

    }

    public static EmailListDto toEmailListDto(List<Member> memberList) {
        return EmailListDto.builder()
                .emailList(memberList.stream()
                        .map(member -> EmailUtil.maskEmail(member.getEmail())).collect(Collectors.toList()))
                .build();
    }

    public static MemberMentionDto toMentionDto(Member member) {
        return MemberMentionDto.builder()
                .id(member.getUserUrl())
                .display(member.getNickname())
                .build();
    }

    public static MemberMentionListDto toMentionListDto(List<Member> memberList) {
        return MemberMentionListDto.builder()
                .mentionList(memberList.stream()
                        .map(MemberConverter::toMentionDto).collect(Collectors.toList()))
                .build();
    }

}

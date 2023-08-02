package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.domain.team.TeamMember;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class MemberSimpleDto {
    private String username;
    private String nickname;
    private UploadFile profileImg;

    static public MemberSimpleDto toDto(Member member) {
        if (member == null) return null;
        return MemberSimpleDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg()).build();
    }
}

package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
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

    @Builder.Default
    private String profileImg = "https://github.com/suddiyo/suddiyo/assets/88311377/4a78ad58-d17a-4e56-9abd-c0848099f9be";

    static public MemberSimpleDto toDto(Member member) {
        return MemberSimpleDto.builder()
                .username(member.getUsername())
                .profileImg(member.getProfileImg()).build();
    }
}

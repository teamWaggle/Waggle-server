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
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;
    private String address;
    private String phone;
    private UploadFile profileImg;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .phone(member.getPhone())
                .profileImg(member.getProfileImg()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .profileImg(profileImg).build();
    }
}

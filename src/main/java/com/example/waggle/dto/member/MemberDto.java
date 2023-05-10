package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.domain.team.TeamMember;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;
    private String address;
    private String phone;
    private String profileImg;
    private List<Pet> pets;
    private List<TeamMember> teamMembers;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .phone(member.getPhone())
                .profileImg(member.getProfileImg())
                .pets(member.getPets())
                .teamMembers(member.getTeamMembers()).build();
    }

    @Builder
    public MemberDto(Long id, String username, String nickname, String address, String phone, String profileImg, List<Pet> pets, List<TeamMember> teamMembers) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.address = address;
        this.phone = phone;
        this.profileImg = profileImg;
        this.pets = pets;
        this.teamMembers = teamMembers;
    }
}

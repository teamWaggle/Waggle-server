package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.team.TeamMember;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;
    private String address;
    private String phone;
    private String profileImg;
    private List<PetDto> pets = new ArrayList<>();
    private List<TeamMember> teamMembers = new ArrayList<>();

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .phone(member.getPhone())
                .profileImg(member.getProfileImg())
                .pets(member.getPets().stream().map(o -> PetDto.toDto(o)).collect(Collectors.toList()))
                .teamMembers(member.getTeamMembers()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .profileImg(profileImg)
                .pets(pets.stream().map(o -> o.toEntity()).collect(Collectors.toList()))
                .teamMembers(teamMembers).build();

    }


    @Builder
    public MemberDto(Long id, String username, String nickname, String address, String phone, String profileImg, List<PetDto> pets, List<TeamMember> teamMembers) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.address = address;
        this.phone = phone;
        this.profileImg = profileImg;
        if (pets != null) this.pets = pets;
        if (teamMembers != null) this.teamMembers = teamMembers;
    }
}

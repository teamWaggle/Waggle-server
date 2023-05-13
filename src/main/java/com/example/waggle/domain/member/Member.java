package com.example.waggle.domain.member;


import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.domain.team.TeamMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String address; // 도로명 주소

    private String phone;

    private String profileImg;

    @OneToMany(mappedBy = "member")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    // question List, answer List, comment List, reply List


    @Builder
    public Member(Long id, String username, String password, String nickname, String address, String phone, String profileImg, List<Pet> pets, List<TeamMember> teamMembers, List<ScheduleMember> scheduleMembers) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.phone = phone;
        this.profileImg = profileImg;
        if (pets != null) this.pets = pets;
        if (teamMembers != null) this.teamMembers = teamMembers;
        if (scheduleMembers != null) this.scheduleMembers = scheduleMembers;
    }

}

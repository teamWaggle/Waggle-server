package com.example.waggle.domain.member;


import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.domain.team.TeamMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String password;

    private String username;

    private String nickname;

    private String address; // 도로명 주소

    private String phone;

    private String profileImg;

    @OneToMany(mappedBy = "member")
    private List<Pet> pets;

    @OneToMany(mappedBy = "member")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    // question List, answer List, comment List, reply List
}

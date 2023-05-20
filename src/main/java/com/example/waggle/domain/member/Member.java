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
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Pet> pets = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    // question List, answer List, comment List, reply List


}

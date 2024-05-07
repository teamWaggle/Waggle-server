package com.example.waggle.domain.member.persistence.entity;


import com.example.waggle.domain.auditing.persistence.entity.BaseTimeEntity;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberProfileDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberUpdateDto;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.domain.schedule.persistence.entity.TeamMember;
import com.example.waggle.security.oauth.factory.OAuth2UserInfoFactory.AuthProvider;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Member extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String userUrl;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    private String name;

    private LocalDate birthday;


    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    public void saveProfileImg(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.getKey()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateInfo(MemberUpdateDto request) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.birthday = request.getBirthday();
        this.profileImgUrl = request.getMemberProfileImg();
    }

    public void registerInfo(MemberProfileDto request) {
        this.userUrl = request.getUserUrl();
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.birthday = request.getBirthday();
        this.profileImgUrl = request.getMemberProfileImg();
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}

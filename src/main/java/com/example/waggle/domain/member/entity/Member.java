package com.example.waggle.domain.member.entity;


import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.global.component.auditing.BaseTimeEntity;
import com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider;
import com.example.waggle.web.dto.member.MemberRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    @GeneratedValue
    @Column(name = "member_id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    private String address;

    private String phone;

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

    public void updateInfo(MemberRequest.Put request, String encodedPassword) {
        this.password = encodedPassword;
        this.address = request.getAddress();
        this.nickname = request.getNickname();
        this.phone = request.getPhone();
        this.profileImgUrl = request.getProfileImg();
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}

package com.example.waggle.domain.member.entity;


import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.schedule.domain.TeamMember;
import com.example.waggle.global.component.auditing.BaseTimeEntity;
import com.example.waggle.web.dto.member.MemberRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id")
public class Member extends BaseTimeEntity implements UserDetails {
    @Id @GeneratedValue
    @Column(name = "member_id", updatable = false, unique = true, nullable = false)
    private Long id;
  
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    private String address;

    private String phone;

    private String profileImgUrl;

//    @Builder.Default
//    @OneToMany(mappedBy = "member")
//    private List<Pet> pets = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Pet> pets = new ArrayList<>();

    //TODO RoomMemeber List


//    public void setPets(List<Pet> pets) {
//        this.pets = pets;
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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

    public void updateInfo(MemberRequest.PutDto request) {
        this.password = request.getPassword();
        this.address = request.getAddress();
        this.nickname = request.getNickname();
        this.phone = request.getPhone();
        this.profileImgUrl = request.getProfileImgUrl();
    }
}

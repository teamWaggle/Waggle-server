package com.example.waggle.domain.member.domain;


import com.example.waggle.global.component.auditing.BaseTimeEntity;
import com.example.waggle.domain.pet.domain.Pet;
import com.example.waggle.global.component.file.UploadFile;
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

    private String nickname;

    private String address;

    private String phone;

    @Embedded
    private UploadFile profileImg;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Pet> pets = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<TeamMember> teamMembers = new ArrayList<>();


    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }



    public void changeProfileImg(UploadFile profileImg) {
        this.profileImg = profileImg;
    }


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
}

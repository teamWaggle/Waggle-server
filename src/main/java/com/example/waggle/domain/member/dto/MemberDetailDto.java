package com.example.waggle.domain.member.dto;

import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.commons.component.file.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    private java.lang.Long id;
    private String username;
    private String nickname;
    private String address;
    private String phone;
    private UploadFile profileImg;

    static public MemberDetailDto toDto(Member member) {
        return MemberDetailDto.builder()
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

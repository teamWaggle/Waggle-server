package com.example.waggle.member.dto;

import com.example.waggle.member.domain.Member;
import com.example.waggle.commons.component.file.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class MemberSummaryDto {
    private String username;
    private String nickname;
    private UploadFile profileImg;

    static public MemberSummaryDto toDto(Member member) {
        if (member == null) return null;
        return MemberSummaryDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg()).build();
    }
}

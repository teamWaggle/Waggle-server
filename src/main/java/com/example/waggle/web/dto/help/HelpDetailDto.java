package com.example.waggle.web.dto.help;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.member.entity.Gender;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelpDetailDto {
    private Long id;
    private String title;
    private String petName;
    private String petKind;
    private int petAge;
    private Gender petGender;
    private String contact;
    private String thumbnail;
    private LocalDateTime lostDate;
    private String content;

    @Builder.Default
    private List<String> medias = new ArrayList<>();
    private String username;

    static public HelpDetailDto toDto(Help help) {
        return HelpDetailDto.builder()
                .id(help.getId())
                .title(help.getTitle())
                .petName(help.getPetName())
                .petKind(help.getPetKind())
                .petAge(help.getPetAge())
                .petGender(help.getPetGender())
                .contact(help.getContact())
                .thumbnail(help.getThumbnail())
                .lostDate(help.getLostDate())
                .content(help.getContent())
                .medias(help.getMedias().stream()
                        .map(h -> h.getUploadFile().getStoreFileName()).collect(Collectors.toList()))
                .username(help.getMember().getUsername())
                .build();
    }
}

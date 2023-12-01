package com.example.waggle.web.dto.help;

import com.example.waggle.domain.board.help.domain.Help;
import com.example.waggle.domain.member.domain.Gender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelpSummaryDto {

    private Long id;
    private String title;
    private String petName;
    private String petKind;
    private int petAge;
    private Gender petGender;
    private String thumbnail;
    private String lostLocate;
    private LocalDateTime lostDate;
    private String username;

    static public HelpSummaryDto toDto(Help help) {
        return HelpSummaryDto.builder()
                .id(help.getId())
                .title(help.getTitle())
                .petName(help.getPetName())
                .petKind(help.getPetKind())
                .petAge(help.getPetAge())
                .petGender(help.getPetGender())
                .thumbnail(help.getThumbnail())
                .lostLocate(help.getLostLocate())
                .lostDate(help.getLostDate())
                .username(help.getMember().getUsername())
                .build();
    }

}

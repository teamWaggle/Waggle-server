package com.example.waggle.web.dto.helpU;

import com.example.waggle.domain.board.helpU.domain.HelpU;
import com.example.waggle.domain.member.domain.Gender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelpUSummaryDto {

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

    static public HelpUSummaryDto toDto(HelpU helpU) {
        return HelpUSummaryDto.builder()
                .id(helpU.getId())
                .title(helpU.getTitle())
                .petName(helpU.getPetName())
                .petKind(helpU.getPetKind())
                .petAge(helpU.getPetAge())
                .petGender(helpU.getPetGender())
                .thumbnail(helpU.getThumbnail())
                .lostLocate(helpU.getLostLocate())
                .lostDate(helpU.getLostDate())
                .username(helpU.getMember().getUsername())
                .build();
    }

}

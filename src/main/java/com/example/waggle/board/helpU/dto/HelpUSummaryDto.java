package com.example.waggle.board.helpU.dto;

import com.example.waggle.board.helpU.domain.HelpU;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.member.domain.Gender;
import jakarta.persistence.Enumerated;
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
    private UploadFile petImage;
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
                .petImage(helpU.getPetImage())
                .lostLocate(helpU.getLostLocate())
                .lostDate(helpU.getLostDate())
                .username(helpU.getMember().getUsername())
                .build();
    }

}

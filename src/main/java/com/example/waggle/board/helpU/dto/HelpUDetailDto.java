package com.example.waggle.board.helpU.dto;

import com.example.waggle.board.helpU.domain.HelpU;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.member.domain.Gender;
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
public class HelpUDetailDto {
    private Long id;
    private String title;
    private String petName;
    private String petKind;
    private int petAge;
    private Gender petGender;
    private String contact;
    private String thumbnail;
    private String lostLocate;
    private LocalDateTime lostDate;
    private String content;
    private String RFID;
    private String characteristic;

    @Builder.Default
    private List<String> medias = new ArrayList<>();
    private String username;

    static public HelpUDetailDto toDto(HelpU helpU) {
        return HelpUDetailDto.builder()
                .id(helpU.getId())
                .title(helpU.getTitle())
                .petName(helpU.getPetName())
                .petKind(helpU.getPetKind())
                .petAge(helpU.getPetAge())
                .petGender(helpU.getPetGender())
                .contact(helpU.getContact())
                .thumbnail(helpU.getThumbnail())
                .lostDate(helpU.getLostDate())
                .lostLocate(helpU.getLostLocate())
                .content(helpU.getContent())
                .RFID(helpU.getRFID())
                .characteristic(helpU.getCharacteristic())
                .medias(helpU.getMedias().stream()
                        .map(h -> h.getUrl()).collect(Collectors.toList()))
                .username(helpU.getMember().getUsername())
                .build();
    }
}

package com.example.waggle.web.dto.helpU;

import com.example.waggle.domain.board.helpU.domain.HelpU;
import com.example.waggle.domain.member.domain.Gender;
import com.example.waggle.domain.member.domain.Member;
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
public class HelpUWriteDto {
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
    private String rfid;
    private String characteristic;
    private String username;

    @Builder.Default
    private List<String> medias = new ArrayList<>();

    public HelpU toEntity(Member member) {
        return HelpU.builder()
                .title(title)
                .petName(petName)
                .petKind(petKind)
                .petAge(petAge)
                .petGender(petGender)
                .contact(contact)
                .thumbnail(thumbnail)
                .lostLocate(lostLocate)
                .lostDate(lostDate)
                .content(content)
                .rfid(rfid)
                .characteristic(characteristic)
                .member(member)
                .build();
    }

    static public HelpUWriteDto toDto(HelpU helpU) {
        return HelpUWriteDto.builder()
                .id(helpU.getId())
                .title(helpU.getTitle())
                .petName(helpU.getPetName())
                .petKind(helpU.getPetKind())
                .petAge(helpU.getPetAge())
                .petGender(helpU.getPetGender())
                .contact(helpU.getContact())
                .thumbnail(helpU.getThumbnail())
                .lostLocate(helpU.getLostLocate())
                .lostDate(helpU.getLostDate())
                .content(helpU.getContent())
                .rfid(helpU.getRfid())
                .characteristic(helpU.getCharacteristic())
                .medias(helpU.getMedias().stream()
                        .map(m -> m.getUploadFile().getStoreFileName()).collect(Collectors.toList()))
                .username(helpU.getMember().getUsername())
                .build();
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
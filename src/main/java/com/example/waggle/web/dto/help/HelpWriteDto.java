package com.example.waggle.web.dto.help;

import com.example.waggle.domain.board.help.entity.Help;
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
public class HelpWriteDto {
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

    public Help toEntity(Member member) {
        return Help.builder()
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

    static public HelpWriteDto toDto(Help help) {
        return HelpWriteDto.builder()
                .id(help.getId())
                .title(help.getTitle())
                .petName(help.getPetName())
                .petKind(help.getPetKind())
                .petAge(help.getPetAge())
                .petGender(help.getPetGender())
                .contact(help.getContact())
                .thumbnail(help.getThumbnail())
                .lostLocate(help.getLostLocate())
                .lostDate(help.getLostDate())
                .content(help.getContent())
                .rfid(help.getRfid())
                .characteristic(help.getCharacteristic())
                .medias(help.getMedias().stream()
                        .map(m -> m.getUploadFile().getStoreFileName()).collect(Collectors.toList()))
                .username(help.getMember().getUsername())
                .build();
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

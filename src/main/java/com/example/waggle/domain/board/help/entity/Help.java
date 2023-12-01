package com.example.waggle.domain.board.help.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.web.dto.help.HelpWriteDto;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.hashtag.domain.BoardHashtag;
import com.example.waggle.domain.member.domain.Gender;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.domain.media.domain.Media;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "help")
@SuperBuilder
@DiscriminatorValue("type_help")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Help extends Board {

    private String title;

    private String petName;
    private String petKind;
    private int petAge;

    @Enumerated
    private Gender petGender;

    private String lostLocate;
    private LocalDateTime lostDate;
    private String characteristic;
    private String rfid;
    private String contact;
    private String thumbnail;

    public Help(Long id, Member member,
                String content, List<BoardHashtag> boardHashtags,
                List<Media> medias, List<Comment> comments,
                String title, String petName, String petKind,
                int petAge, Gender petGender, String lostLocate,
                LocalDateTime lostDate, String characteristic,
                String rfid, String contact, String thumbnail) {
        super(id, member, content, boardHashtags, medias, comments);
        this.title = title;
        this.petName = petName;
        this.petKind = petKind;
        this.petAge = petAge;
        this.petGender = petGender;
        this.lostLocate = lostLocate;
        this.lostDate = lostDate;
        this.characteristic = characteristic;
        this.rfid = rfid;
        this.contact = contact;
        this.thumbnail = thumbnail;
    }

    public void changeHelp(HelpWriteDto helpUWriteDto) {
        this.title = helpUWriteDto.getTitle();
        this.petName = helpUWriteDto.getPetName();
        this.petKind = helpUWriteDto.getPetKind();
        this.petAge = helpUWriteDto.getPetAge();
        this.petGender = helpUWriteDto.getPetGender();
        this.lostLocate = helpUWriteDto.getLostLocate();
        this.lostDate = helpUWriteDto.getLostDate();
        this.characteristic = helpUWriteDto.getCharacteristic();
        this.rfid = helpUWriteDto.getRfid();
        this.contact = helpUWriteDto.getContact();
        //this.thumbnail = helpUWriteDto.getThumbnail();
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}

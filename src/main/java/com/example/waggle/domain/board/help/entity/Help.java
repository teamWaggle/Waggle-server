package com.example.waggle.domain.board.help.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.web.dto.help.HelpRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "help")
@SuperBuilder
@DiscriminatorValue("type_help")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Help extends Board {

    private String title;
    private String petKind;
    private int petAge;
    @Enumerated(EnumType.STRING)
    private Gender petGender;
    private LocalDateTime lostDate;
    private String lostLocate;
    private String contact;
    private String thumbnail;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

//    public Help(Long id, Member member,
//                String content, List<BoardHashtag> boardHashtags,
//                List<Media> medias, String title, String petKind,
//                int petAge, Gender petGender, Category category,
//                LocalDateTime lostDate, String lostLocate,String contact, String thumbnail) {
//        super(id, member, content, boardHashtags, medias);
//        this.title = title;
//        this.petKind = petKind;
//        this.petAge = petAge;
//        this.petGender = petGender;
//        this.category = category;
//        this.lostDate = lostDate;
//        this.lostLocate = lostLocate;
//        this.contact = contact;
//        this.thumbnail = thumbnail;
//    }

    public void changeHelp(HelpRequest.Post helpUWriteDto) {
        this.title = helpUWriteDto.getTitle();
        this.petKind = helpUWriteDto.getPetKind();
        this.petAge = helpUWriteDto.getPetAge();
        this.petGender = helpUWriteDto.getPetGender();
        this.lostDate = helpUWriteDto.getLostDate();
        this.lostLocate = helpUWriteDto.getLostLocate();
        this.contact = helpUWriteDto.getContact();
        this.content = helpUWriteDto.getContent();
        this.thumbnail = helpUWriteDto.getThumbnail();
        this.category = helpUWriteDto.getCategory();
    }

}

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

    @Column(nullable = false)
    private String title;
    private String petKind;
    private int petAge;
    @Enumerated(EnumType.STRING)
    private Gender petGender;
    private LocalDateTime lostDate;
    private String lostLocate;
    private String contact;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public void changeHelp(HelpRequest.Put helpUpdateDto) {
        this.title = helpUpdateDto.getTitle();
        this.petKind = helpUpdateDto.getPetKind();
        this.petAge = helpUpdateDto.getPetAge();
        this.petGender = helpUpdateDto.getPetGender();
        this.lostDate = helpUpdateDto.getLostDate();
        this.lostLocate = helpUpdateDto.getLostLocate();
        this.contact = helpUpdateDto.getContact();
        this.content = helpUpdateDto.getContent();
        this.category = helpUpdateDto.getCategory();
    }

    public enum Category {
        FIND_PET, FIND_OWNER, PROTECT, ETC
    }

}

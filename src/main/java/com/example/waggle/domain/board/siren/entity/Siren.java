package com.example.waggle.domain.board.siren.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.web.dto.siren.SirenRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "siren")
@SuperBuilder
@DiscriminatorValue("type_siren")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Siren extends Board {

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

    public void changeSiren(SirenRequest.Put sirenUpdateDto) {
        this.title = sirenUpdateDto.getTitle();
        this.petKind = sirenUpdateDto.getPetKind();
        this.petAge = sirenUpdateDto.getPetAge();
        this.petGender = sirenUpdateDto.getPetGender();
        this.lostDate = sirenUpdateDto.getLostDate();
        this.lostLocate = sirenUpdateDto.getLostLocate();
        this.contact = sirenUpdateDto.getContact();
        this.content = sirenUpdateDto.getContent();
        this.category = sirenUpdateDto.getCategory();
    }

    public enum Category {
        FIND_PET, FIND_OWNER, PROTECT, ETC
    }
}

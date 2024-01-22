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

    public void changeSiren(SirenRequest.Post request) {
        this.title = request.getTitle();
        this.petKind = request.getPetKind();
        this.petAge = request.getPetAge();
        this.petGender = request.getPetGender();
        this.lostDate = request.getLostDate();
        this.lostLocate = request.getLostLocate();
        this.contact = request.getContact();
        this.content = request.getContent();
        this.category = request.getCategory();
    }

    public enum Category {
        FIND_PET, FIND_OWNER, PROTECT, ETC
    }
}

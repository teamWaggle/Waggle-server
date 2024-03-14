package com.example.waggle.domain.board.siren.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.web.dto.siren.SirenRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "siren")
@SuperBuilder
@DiscriminatorValue("type_siren")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Siren extends Board {

    @Column(nullable = false)
    private String title;
    private String petBreed;
    private String petAge;
    @Enumerated(EnumType.STRING)
    private Gender petGender;
    private LocalDate lostDate;
    private String lostLocate;
    private String contact;

    @Builder.Default
    private int viewCount = 0;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SirenCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResolutionStatus status;

    public void changeSiren(SirenRequest updateSirenRequest) {
        this.title = updateSirenRequest.getTitle();
        this.petBreed = updateSirenRequest.getPetBreed();
        this.petAge = updateSirenRequest.getPetAge();
        this.petGender = Gender.valueOf(updateSirenRequest.getPetGender());
        this.lostDate = updateSirenRequest.getLostDate();
        this.lostLocate = updateSirenRequest.getLostLocate();
        this.contact = updateSirenRequest.getContact();
        this.content = updateSirenRequest.getContent();
        this.category = SirenCategory.valueOf(updateSirenRequest.getCategory());
    }

    public void changeStatus(ResolutionStatus status) {
        this.status = status;
    }

    public void increaseViewCount() {
        viewCount++;
    }

}

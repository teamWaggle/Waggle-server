package com.example.waggle.board.helpU.domain;

import com.example.waggle.board.Board;
import com.example.waggle.comment.domain.Comment;
import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.hashtag.domain.BoardHashtag;
import com.example.waggle.media.domain.Media;
import com.example.waggle.member.domain.Gender;
import com.example.waggle.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_helpU")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelpU extends Board {

    @Id
    @GeneratedValue
    @Column(name = "sos_id")
    private Long id;

    private String title;

    private String petName;
    private String petKind;
    private int petAge;

    @Enumerated
    private Gender petGender;

    private String lostLocate;
    private LocalDateTime lostDate;
    private String characteristic;
    private String RFID;
    private String contact;
    private String petImage;

    public HelpU(Long id, Member member,
                 String content, List<BoardHashtag> boardHashtags,
                 List<Media> medias, List<Comment> comments,
                 String title, String petName, String petKind,
                 int petAge, Gender petGender, String lostLocate,
                 LocalDateTime lostDate, String characteristic,
                 String RFID, String contact, String petImage) {
        super(id, member, content, boardHashtags, medias, comments);
        this.title = title;
        this.petName = petName;
        this.petKind = petKind;
        this.petAge = petAge;
        this.petGender = petGender;
        this.lostLocate = lostLocate;
        this.lostDate = lostDate;
        this.characteristic = characteristic;
        this.RFID = RFID;
        this.contact = contact;
        this.petImage = petImage;
    }

}

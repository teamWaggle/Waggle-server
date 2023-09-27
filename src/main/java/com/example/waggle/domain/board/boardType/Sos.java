package com.example.waggle.domain.board.boardType;

import com.example.waggle.component.auditing.BaseEntity;
import com.example.waggle.domain.member.Gender;
import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sos extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "sos_id")
    private Long id;

    //@Temporal(TemporalType.TIMESTAMP)
    protected LocalDateTime createdDate;

    @Lob
    protected String content;

    private String name;
    private String kind;
    private int age;

    @Enumerated
    private Gender gender;

    private String lostLocate;
    private LocalDateTime lostDate;
    private String characteristic;
    private String RFID;
    private String contact;
    private String petImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    protected Member member;

}

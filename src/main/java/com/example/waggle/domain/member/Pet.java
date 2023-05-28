package com.example.waggle.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Pet {
    @Id @GeneratedValue
    @Column(name = "pet_id")
    private Long id;

    private String name;

    private String breed;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private LocalDateTime birthday;

    private String profileImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}

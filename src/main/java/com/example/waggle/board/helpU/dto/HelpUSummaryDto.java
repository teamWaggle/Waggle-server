package com.example.waggle.board.helpU.dto;

import com.example.waggle.member.domain.Gender;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HelpUSummaryDto {

    private String title;
    private String name;
    private String kind;
    private int age;
    private Gender gender;
    private String contact;
    private String petImage;

    public HelpUSummaryDto(String title, String name, String kind,
                           int age, Gender gender, String contact, String petImage) {

    }

}

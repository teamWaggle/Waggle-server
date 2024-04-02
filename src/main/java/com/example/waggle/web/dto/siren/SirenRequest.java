package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.global.annotation.valid.enums.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class SirenRequest {
    private String title;
    private String petBreed;
    private String petAge;

    @ValidEnum(target = Gender.class)
    private String petGender;

    private String contact;
    private String lostLocate;
    private LocalDate lostDate;
    private String content;
    private List<String> mediaList;

    @ValidEnum(target = SirenCategory.class)
    private String category;
}

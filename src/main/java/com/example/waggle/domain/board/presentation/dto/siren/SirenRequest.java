package com.example.waggle.domain.board.presentation.dto.siren;

import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import com.example.waggle.domain.member.persistence.entity.Gender;
import com.example.waggle.global.annotation.validation.enums.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class SirenRequest {

    @NotEmpty(message = "제목을 입력해 주세요.")
    @Size(max = 20, message = "제목은 최대 20자까지 입력할 수 있습니다.")
    private String title;

    @Size(max = 15, message = "반려동물 품종은 최대 15자까지 입력할 수 있습니다.")
    private String petBreed;

    @Size(max = 15, message = "반려동물 나이는 최대 15자까지 입력할 수 있습니다.")
    private String petAge;

    @ValidEnum(target = Gender.class, message = "유효한 성별을 선택해주세요.")
    private String petGender;

    @Size(max = 30, message = "연락처는 최대 30자까지 입력할 수 있습니다.")
    private String contact;

    @Size(max = 15, message = "실종 위치는 최대 15자까지 입력할 수 있습니다.")
    private String lostLocate;

    private LocalDate lostDate;

    @NotEmpty(message = "내용을 작성해 주세요.")
    @Size(max = 1500, message = "내용은 최대 1500자까지 입력할 수 있습니다.")
    private String content;

    @Size(max = 10, message = "미디어는 최대 10개까지 추가할 수 있습니다.")
    private List<String> mediaList;

    @ValidEnum(target = SirenCategory.class, message = "유효한 카테고리를 선택해주세요.")
    private String category;
}

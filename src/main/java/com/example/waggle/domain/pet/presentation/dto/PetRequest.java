package com.example.waggle.domain.pet.presentation.dto;

import com.example.waggle.domain.member.persistence.entity.Gender;
import com.example.waggle.global.annotation.validation.enums.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class PetRequest {

    @NotEmpty(message = "반려동물 이름을 입력해주세요.")
    @Size(max = 20, message = "이름은 최대 20자까지 입력할 수 있습니다.")
    private String name;

    @Size(max = 100, message = "설명은 최대 100자까지 입력할 수 있습니다.")
    private String description;

    @Size(max = 50, message = "품종은 최대 50자까지 입력할 수 있습니다.")
    private String breed;

    @Size(max = 10, message = "나이는 최대 10자까지 입력할 수 있습니다.")
    private String age;

    @ValidEnum(target = Gender.class, message = "유효한 성별을 선택해주세요.")
    private String gender;

    private String petProfileImg;

}
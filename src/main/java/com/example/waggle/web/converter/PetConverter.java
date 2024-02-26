package com.example.waggle.web.converter;

import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.global.util.MediaUtil;

import com.example.waggle.web.dto.pet.PetResponse.PetDetailDto;
import com.example.waggle.web.dto.pet.PetResponse.PetSummaryDto;
import java.util.List;
import java.util.stream.Collectors;

public class PetConverter {
    public static PetSummaryDto toSummaryDto(Pet pet) {
        return PetSummaryDto.builder()
                .petId(pet.getId())
                .profileImgUrl(MediaUtil.getProfileImg(pet))
                .name(pet.getName())
                .gender(pet.getGender())
                .build();
    }

    public static PetDetailDto toDetailDto(Pet pet) {
        return PetDetailDto.builder()
                .petId(pet.getId())
                .breed(pet.getBreed())
                .age(pet.getAge())
                .gender(pet.getGender())
                .name(pet.getName())
                .profileImgUrl(MediaUtil.getProfileImg(pet))
                .build();
    }

    public static List<PetSummaryDto> toListDto(List<Pet> pets) {
        return pets.stream().map(PetConverter::toSummaryDto).collect(Collectors.toList());
    }
}

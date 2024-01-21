package com.example.waggle.web.converter;

import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.pet.PetResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PetConverter {
    public static PetResponse.SummaryDto toSummaryDto(Pet pet) {
        return PetResponse.SummaryDto.builder()
                .id(pet.getId())
                .profileImgUrl(MediaUtil.getProfileImg(pet))
                .name(pet.getName())
                .gender(pet.getGender())
                .build();
    }

    public static PetResponse.DetailDto toDetailDto(Pet pet) {
        return PetResponse.DetailDto.builder()
                .id(pet.getId())
                .breed(pet.getBreed())
                .birthday(pet.getBirthday())
                .gender(pet.getGender())
                .name(pet.getName())
                .profileImgUrl(MediaUtil.getProfileImg(pet))
                .build();
    }

    public static List<PetResponse.SummaryDto> toListDto(List<Pet> pets) {
        return pets.stream().map(PetConverter::toSummaryDto).collect(Collectors.toList());
    }
}

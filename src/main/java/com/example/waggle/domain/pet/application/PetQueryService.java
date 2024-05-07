package com.example.waggle.domain.pet.application;

import com.example.waggle.domain.pet.persistence.entity.Pet;

import java.util.List;

public interface PetQueryService {

    Pet getPetById(Long petId);

    List<Pet> getPetsByUserUrl(String userUrl);

    List<Pet> getPetsByMemberId(Long memberId);

}

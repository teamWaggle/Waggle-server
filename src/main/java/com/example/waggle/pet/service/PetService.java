package com.example.waggle.pet.service;

import com.example.waggle.pet.dto.PetDto;

public interface PetService {

    PetDto getPetById(Long petId);

    Long createPet(PetDto petDto);

    Long updatePet(Long petId, PetDto petDto);

    void deletePet(Long petId);
}

package com.example.waggle.pet.service;

import com.example.waggle.pet.dto.PetDto;

public interface PetCommandService {

    Long createPet(PetDto petDto);

    Long updatePet(Long petId, PetDto petDto);

    void deletePet(Long petId);

}

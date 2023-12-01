package com.example.waggle.domain.pet.service;

import com.example.waggle.web.dto.pet.PetDto;

public interface PetCommandService {

    Long createPet(PetDto petDto);

    Long updatePet(Long petId, PetDto petDto);

    void deletePet(Long petId);

}

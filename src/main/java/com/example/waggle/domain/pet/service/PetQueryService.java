package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.pet.dto.PetDto;

public interface PetQueryService {

    PetDto getPetById(Long petId);

}

package com.example.waggle.pet.service;

import com.example.waggle.pet.dto.PetDto;

public interface PetQueryService {

    PetDto getPetById(Long petId);

}

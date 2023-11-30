package com.example.waggle.domain.pet.service;

import com.example.waggle.web.dto.pet.PetDto;

public interface PetQueryService {

    PetDto getPetById(Long petId);

}

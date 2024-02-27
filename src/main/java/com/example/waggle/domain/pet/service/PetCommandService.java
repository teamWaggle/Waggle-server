package com.example.waggle.domain.pet.service;

import com.example.waggle.web.dto.pet.PetRequest;

public interface PetCommandService {

    Long createPet(PetRequest createPetRequest);

    Long createPetByUsername(PetRequest createPetRequest, String username);

    Long updatePet(Long petId, PetRequest updatePetRequest);

    Long updatePetByUsername(Long petId, String username, PetRequest updatePetRequest);

    void deletePet(Long petId);

    void deletePetByUsername(Long petId, String username);

    void deleteAllPetByUser(String username);
}

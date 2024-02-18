package com.example.waggle.domain.pet.service;

import com.example.waggle.web.dto.pet.PetRequest;

public interface PetCommandService {

    Long createPet(PetRequest.Post petDto);

    Long createPetByUsername(PetRequest.Post petDto, String username);

    void createPets(PetRequest.PostList petDto, String username);

    Long updatePet(Long petId, PetRequest.Post petDto);

    Long updatePetByUsername(Long petId, String username, PetRequest.Post petDto);

    void deletePet(Long petId);

    void deletePetByUsername(Long petId, String username);

    void deleteAllPetByUser(String username);
}

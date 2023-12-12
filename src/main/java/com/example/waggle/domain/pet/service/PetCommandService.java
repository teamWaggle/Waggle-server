package com.example.waggle.domain.pet.service;

import com.example.waggle.web.dto.pet.PetRequest;

import java.util.List;

public interface PetCommandService {

    Long createPet(PetRequest.Post petDto);

    void createPets(List<PetRequest.Post> petDto, String username);

    Long updatePet(Long petId, PetRequest.Post petDto);

    void deletePet(Long petId);

    void deleteAllPetByUser();
}

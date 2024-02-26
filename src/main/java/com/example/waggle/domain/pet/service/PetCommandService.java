package com.example.waggle.domain.pet.service;

import com.example.waggle.web.dto.pet.PetRequest.PetCreateDto;
import com.example.waggle.web.dto.pet.PetRequest.PetListCreateDto;

public interface PetCommandService {

    Long createPet(PetCreateDto petDto);

    Long createPetByUsername(PetCreateDto petDto, String username);

    void createPets(PetListCreateDto petDto, String username);

    Long updatePet(Long petId, PetCreateDto petDto);

    Long updatePetByUsername(Long petId, String username, PetCreateDto petDto);

    void deletePet(Long petId);

    void deletePetByUsername(Long petId, String username);

    void deleteAllPetByUser(String username);
}

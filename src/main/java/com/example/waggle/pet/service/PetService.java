package com.example.waggle.pet.service;

import com.example.waggle.pet.dto.PetDto;

import java.util.Optional;

public interface PetService {

    public Optional<PetDto> findByPetId(Long petId);

    public PetDto addPet(PetDto petDto);

    public PetDto updatePet(Long petId, PetDto petDto);

    public Boolean removePet(Long petId);
}

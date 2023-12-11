package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.pet.entity.Pet;

import java.util.List;

public interface PetQueryService {

    Pet getPetById(Long petId);

    List<Pet> getPetsByUsername(String username);

}

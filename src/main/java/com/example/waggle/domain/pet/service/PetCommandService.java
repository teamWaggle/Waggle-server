package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.pet.PetRequest;

public interface PetCommandService {

    Long createPet(PetRequest.Post petDto);

    Long createPet(Member member, PetRequest.Post petDto);

    void createPets(PetRequest.PostList petDto, String username);

    Long updatePet(Long petId, PetRequest.Post petDto);

    Long updatePet(Long petId, Member member, PetRequest.Post petDto);

    void deletePet(Long petId);

    void deletePet(Long petId, Member member);

    void deleteAllPetByUser(String username);
}

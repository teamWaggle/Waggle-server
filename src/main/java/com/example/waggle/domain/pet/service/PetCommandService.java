package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.pet.PetRequest;

public interface PetCommandService {

    Long createPet(PetRequest createPetRequest, Member member);

    Long updatePet(Long petId, PetRequest updatePetRequest, Member member);

    void deletePet(Long petId, Member member);

    void deleteAllPetByUser(String username);

}

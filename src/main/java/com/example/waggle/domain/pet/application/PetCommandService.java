package com.example.waggle.domain.pet.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.pet.presentation.dto.PetRequest;

public interface PetCommandService {

    Long createPet(PetRequest createPetRequest, Member member);

    Long updatePet(Long petId,
                   PetRequest updatePetRequest,
                   Member member);

    void deletePet(Long petId, Member member);

    void deletePetByAdmin(Long petId, Member member);

}

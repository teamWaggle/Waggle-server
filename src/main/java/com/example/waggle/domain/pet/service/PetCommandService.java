package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.pet.PetRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PetCommandService {

    Long createPet(PetRequest createPetRequest, MultipartFile petProfileImg, Member member);

    Long updatePet(Long petId,
                   PetRequest updatePetRequest,
                   MultipartFile petProfileImg,
                   boolean allowUpload,
                   Member member);

    void deletePet(Long petId, Member member);

    void deleteAllPetByUser(String username);

}

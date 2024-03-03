package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.global.exception.handler.PetHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.pet.PetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class PetCommandServiceImpl implements PetCommandService {

    private final PetRepository petRepository;
    private final MemberQueryService memberQueryService;
    private final AwsS3Service awsS3Service;

    @Override
    public Long createPet(PetRequest createPetRequest, MultipartFile petProfileImg, Member member) {
        String profileImgUrl = MediaUtil.saveProfileImg(petProfileImg, awsS3Service);
        Pet pet = buildPet(createPetRequest, profileImgUrl, member);
        petRepository.save(pet);
        return pet.getId();
    }

    @Override
    public Long updatePet(Long petId,
                          PetRequest updatePetRequest,
                          MultipartFile petProfileImg,
                          boolean allowUpload,
                          Member member) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        validateIsOwner(member, pet);
        String profileImgUrl = updateProfileImg(petProfileImg, allowUpload, pet);
        pet.update(updatePetRequest, profileImgUrl);
        return pet.getId();
    }

    @Override
    public void deletePet(Long petId, Member member) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        validateIsOwner(member, pet);
        petRepository.delete(pet);
    }

    @Override
    public void deleteAllPetByUser(String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        List<Pet> pets = petRepository.findByMemberUsername(member.getUsername());
        pets.stream().forEach(pet -> petRepository.delete(pet));
    }


    private void validateIsOwner(Member member, Pet pet) {
        if (!pet.getMember().equals(member)) {
            throw new PetHandler(ErrorStatus.PET_INFO_CANNOT_EDIT_OTHERS);
        }
    }

    private Pet buildPet(PetRequest createPetRequest, String profileImg, Member member) {
        return Pet.builder()
                .age(createPetRequest.getAge())
                .name(createPetRequest.getName())
                .description(createPetRequest.getDescription())
                .breed(createPetRequest.getBreed())
                .gender(Gender.valueOf(createPetRequest.getGender()))
                .profileImgUrl(profileImg)
                .member(member)
                .build();
    }

    private String updateProfileImg(MultipartFile petProfileImg, boolean allowUpload, Pet pet) {
        if (!allowUpload) {
            return pet.getProfileImgUrl();
        }
        if (pet.getProfileImgUrl() != null) {
            awsS3Service.deleteFile(pet.getProfileImgUrl());
        }
        return MediaUtil.saveProfileImg(petProfileImg, awsS3Service);
    }

}

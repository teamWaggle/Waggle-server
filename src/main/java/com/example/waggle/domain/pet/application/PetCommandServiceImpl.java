package com.example.waggle.domain.pet.application;

import com.example.waggle.domain.member.application.MemberQueryService;
import com.example.waggle.domain.member.persistence.entity.Gender;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.pet.persistence.dao.PetRepository;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.domain.pet.presentation.dto.PetRequest;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.object.handler.PetHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class PetCommandServiceImpl implements PetCommandService {

    private final PetRepository petRepository;
    private final MemberQueryService memberQueryService;

    @Override
    public Long createPet(PetRequest createPetRequest, Member member) {
        Pet pet = buildPet(createPetRequest, member);
        petRepository.save(pet);
        return pet.getId();
    }

    @Override
    public Long updatePet(Long petId,
                          PetRequest updatePetRequest,
                          Member member) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        validateIsOwner(member, pet);
        pet.update(updatePetRequest);
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
    public void deletePetByAdmin(Long petId, Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new MemberHandler(ErrorStatus.MEMBER_REQUEST_IS_UNACCEPTABLE_BECAUSE_OF_AUTHORIZATION);
        }
        petRepository.deleteById(petId);
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

    private Pet buildPet(PetRequest createPetRequest, Member member) {
        return Pet.builder()
                .age(createPetRequest.getAge())
                .name(createPetRequest.getName())
                .description(createPetRequest.getDescription())
                .breed(createPetRequest.getBreed())
                .gender(Gender.valueOf(createPetRequest.getGender()))
                .profileImgUrl(createPetRequest.getPetProfileImg())
                .member(member)
                .build();
    }

}

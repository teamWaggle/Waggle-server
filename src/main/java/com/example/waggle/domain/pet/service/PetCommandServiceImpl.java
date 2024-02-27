package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.global.exception.handler.PetHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.pet.PetRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class PetCommandServiceImpl implements PetCommandService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final MemberQueryService memberQueryService;

    @Override
    public Long createPet(PetRequest createPetRequest) {
        Member member = memberQueryService.getSignInMember();
        Pet build = buildPet(createPetRequest, member);
        Pet pet = petRepository.save(build);
        return pet.getId();
    }

    @Override
    public Long createPetByUsername(PetRequest createPetRequest, String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        Pet build = buildPet(createPetRequest, member);
        Pet pet = petRepository.save(build);
        return pet.getId();
    }

    @Override
    public Long updatePet(Long petId, PetRequest updatePetRequest) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        if (!pet.getMember().getUsername().equals(SecurityUtil.getCurrentUsername())) {
            throw new PetHandler(ErrorStatus.PET_INFO_CANNOT_EDIT_OTHERS);
        }
        pet.update(updatePetRequest);
        return pet.getId();
    }

    @Override
    public Long updatePetByUsername(Long petId, String username, PetRequest updatePetRequest) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        if (!pet.getMember().getUsername().equals(username)) {
            throw new PetHandler(ErrorStatus.PET_INFO_CANNOT_EDIT_OTHERS);
        }
        pet.update(updatePetRequest);
        return pet.getId();
    }

    @Override
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        petRepository.delete(pet);
    }

    @Override
    public void deletePetByUsername(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        if (!pet.getMember().getUsername().equals(username)) {
            throw new PetHandler(ErrorStatus.PET_INFO_CANNOT_EDIT_OTHERS);
        }
        petRepository.delete(pet);
    }

    @Override
    public void deleteAllPetByUser(String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        List<Pet> pets =
                petRepository.findByMemberUsername(member.getUsername());
        pets.stream().forEach(pet -> petRepository.delete(pet));
    }

    private static Pet buildPet(PetRequest petDto, Member member) {
        Pet build = Pet.builder()
                .age(petDto.getAge())
                .name(petDto.getName())
                .breed(petDto.getBreed())
                .gender(Gender.valueOf(petDto.getGender()))
                .member(member)
                .profileImgUrl(petDto.getProfileImgUrl())
                .build();
        return build;
    }

}

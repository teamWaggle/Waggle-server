package com.example.waggle.domain.pet.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.PetHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.pet.PetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class PetCommandServiceImpl implements PetCommandService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final MemberQueryService memberQueryService;

    @Override
    public Long createPet(PetRequest.Post petDto) {
        Member member = memberQueryService.getSignInMember();
        Pet build = buildPet(petDto, member);
        Pet pet = petRepository.save(build);
        return pet.getId();
    }

    @Override
    public void createPets(List<PetRequest.Post> petList, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        petList.stream().forEach(pet -> petRepository.save(buildPet(pet,member)));
    }

    @Override
    public Long updatePet(Long petId, PetRequest.Post petDto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        pet.update(petDto);
        return pet.getId();
    }

    @Override
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
        petRepository.delete(pet);
    }

    @Override
    public void deleteAllPetByUser() {
        Member member = memberQueryService.getSignInMember();
        List<Pet> pets =
                petRepository.findByMemberUsername(member.getUsername());
        pets.stream().forEach(pet -> petRepository.delete(pet));
    }

    private static Pet buildPet(PetRequest.Post petDto, Member member) {
        Pet build = Pet.builder()
                .birthday(petDto.getBirthday())
                .name(petDto.getName())
                .breed(petDto.getBreed())
                .gender(petDto.getGender())
                .member(member)
                .profileImg(petDto.getProfileImg())
                .build();
        return build;
    }

}

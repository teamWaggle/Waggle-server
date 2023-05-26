package com.example.waggle.service.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.repository.member.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PetService {
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    public Optional<PetDto> findByPetId(Long petId) {
        Optional<Pet> findPet = petRepository.findById(petId);
        return findPet.map(PetDto::toDto);
    }

    List<PetDto> findByMemberId(Long memberId) {
        List<Pet> petList = petRepository.findByMemberId(memberId);
        return petList.stream().map(PetDto::toDto).collect(Collectors.toList());
    }

    List<PetDto> findByUsername(String username) {
        List<Pet> petList = petRepository.findByUsername(username);
        return petList.stream().map(PetDto::toDto).collect(Collectors.toList());
    }



    @Transactional
    public PetDto addPet(PetDto petDto) {
        Member member = memberRepository.findByUsername(petDto.getUsername()).get();
        Pet pet = petRepository.save(petDto.toEntity(member));
        return PetDto.toDto(pet);
    }

    @Transactional
    public PetDto updatePet(Long petId, PetDto petDto) {
        Optional<Pet> findPet = petRepository.findById(petId);
        if (findPet.isPresent()) {
            Pet pet = findPet.get();

            Pet updatedPet = Pet.builder()
                    .id(pet.getId())
                    .name(petDto.getName())
                    .breed(petDto.getBreed())
                    .sex(petDto.getSex())
                    .birthday(petDto.getBirthday())
                    .profileImg(petDto.getProfileImg())
                    .member(pet.getMember())
                    .build();

            // 업데이트 된 펫 저장
            Pet savedPet = petRepository.save(updatedPet);
            return PetDto.toDto(savedPet);
        } else {
            // 존재하지 않는 펫 처리
            return null;
        }
    }

    @Transactional
    public Boolean removePet(Long petId) {
        Optional<Pet> removalPet = petRepository.findById(petId);
        if(removalPet.isPresent()) {
            Pet pet = removalPet.get();
            petRepository.delete(pet);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}

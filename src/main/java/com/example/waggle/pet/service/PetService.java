package com.example.waggle.pet.service;

import com.example.waggle.member.domain.Member;
import com.example.waggle.pet.domain.Pet;
import com.example.waggle.pet.dto.PetDto;
import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.waggle.commons.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.PET_NOT_FOUND;

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

    @Transactional
    public PetDto addPet(PetDto petDto) {
        Member member = memberRepository.findByUsername(petDto.getUsername())
                .orElseThrow(() -> new CustomPageException(MEMBER_NOT_FOUND));
        Pet pet = petRepository.save(petDto.toEntity(member));
        return PetDto.toDto(pet);
    }

    @Transactional
    public PetDto updatePet(Long petId, PetDto petDto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomAlertException(PET_NOT_FOUND));
        pet.update(petDto);
        Pet updatedPet = petRepository.findById(petId).get();
        return PetDto.toDto(updatedPet);
    }

    @Transactional
    public Boolean removePet(Long petId) {
        Optional<Pet> petToRemove = petRepository.findById(petId);
        if(petToRemove.isPresent()) {
            Pet pet = petToRemove.get();
            petRepository.delete(pet);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}

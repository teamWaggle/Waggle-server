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

    @Transactional
    public PetDto addPet(PetDto petDto) {
        Member member = memberRepository.findByUsername(petDto.getUsername()).get();
        Pet pet = petRepository.save(petDto.toEntity(member));
        return PetDto.toDto(pet);
    }

    @Transactional
    public PetDto updatePet(Long petId, PetDto petDto) {
        Optional<Pet> petToUpdate = petRepository.findById(petId);
        if (petToUpdate.isPresent()) {
            Pet pet = petToUpdate.get();
            pet.update(petDto);
            Pet updatedPet = petRepository.findById(petId).get();
            return PetDto.toDto(updatedPet);
        } else {
            // TODO 예외 처리
            return null;
        }
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

package com.example.waggle.domain.pet.service;

import static com.example.waggle.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.waggle.global.exception.ErrorCode.PET_NOT_FOUND;

import com.example.waggle.global.exception.CustomAlertException;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.web.dto.pet.PetDto;
import com.example.waggle.domain.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class PetCommandServiceImpl implements PetCommandService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    @Override
    public Long createPet(PetDto petDto) {
        Member member = memberRepository.findByUsername(petDto.getUsername())
                .orElseThrow(() -> new CustomPageException(MEMBER_NOT_FOUND));
        Pet pet = petRepository.save(petDto.toEntity(member));
        return pet.getId();
    }

    @Override
    public Long updatePet(Long petId, PetDto petDto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomAlertException(PET_NOT_FOUND));
        pet.update(petDto);
        return pet.getId();
    }

    @Override
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomAlertException(PET_NOT_FOUND));
        petRepository.delete(pet);
    }

}

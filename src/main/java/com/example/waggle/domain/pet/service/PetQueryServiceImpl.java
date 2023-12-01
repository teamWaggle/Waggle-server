package com.example.waggle.domain.pet.service;

import static com.example.waggle.global.exception.ErrorCode.PET_NOT_FOUND;

import com.example.waggle.global.exception.CustomAlertException;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.web.dto.pet.PetDto;
import com.example.waggle.domain.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PetQueryServiceImpl implements PetQueryService{

    private final PetRepository petRepository;

    @Override
    public PetDto getPetById(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomAlertException(PET_NOT_FOUND));
        return PetDto.toDto(pet);
    }

}

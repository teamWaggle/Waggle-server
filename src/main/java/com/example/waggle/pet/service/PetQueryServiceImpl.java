package com.example.waggle.pet.service;

import static com.example.waggle.commons.exception.ErrorCode.PET_NOT_FOUND;

import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.pet.domain.Pet;
import com.example.waggle.pet.dto.PetDto;
import com.example.waggle.pet.repository.PetRepository;
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

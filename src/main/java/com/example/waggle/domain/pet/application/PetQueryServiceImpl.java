package com.example.waggle.domain.pet.application;

import com.example.waggle.domain.pet.persistence.dao.PetRepository;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.exception.object.handler.PetHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PetQueryServiceImpl implements PetQueryService {

    private final PetRepository petRepository;

    @Override
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new PetHandler(ErrorStatus.PET_NOT_FOUND));
    }

    @Override
    public List<Pet> getPetsByUserUrl(String userUrl) {
        return petRepository.findByMemberUserUrl(userUrl);
    }

    @Override
    public List<Pet> getPetsByMemberId(Long memberId) {
        return petRepository.findPetsByMemberId(memberId);
    }
}

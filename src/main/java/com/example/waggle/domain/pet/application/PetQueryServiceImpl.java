package com.example.waggle.domain.pet.application;

import com.example.waggle.domain.pet.persistence.dao.PetRepository;
import com.example.waggle.domain.pet.persistence.entity.Pet;
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
    public List<Pet> getPetsByUserUrl(String userUrl) {
        return petRepository.findByMemberUserUrl(userUrl);
    }
}

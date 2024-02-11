package com.example.waggle.domain.pet.repository;

import com.example.waggle.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByMemberUsername(String username);

    List<Pet> findPetsByMemberId(Long memberId);
}

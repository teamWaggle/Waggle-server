package com.example.waggle.domain.pet.repository;

import com.example.waggle.domain.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}

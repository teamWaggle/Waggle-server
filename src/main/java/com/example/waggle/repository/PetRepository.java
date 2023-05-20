package com.example.waggle.repository;

import com.example.waggle.domain.member.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {


}

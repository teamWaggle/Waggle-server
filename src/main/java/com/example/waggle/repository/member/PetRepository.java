package com.example.waggle.repository.member;

import com.example.waggle.domain.member.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p FROM Pet p WHERE p.member.id = :memberId")
    List<Pet> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Pet p WHERE p.member.username = :username")
    List<Pet> findByUsername(@Param("username") String username);


}

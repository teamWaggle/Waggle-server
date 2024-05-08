package com.example.waggle.domain.pet.persistence.dao;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByMemberUsername(String username);

    List<Pet> findByMemberUserUrl(String userUrl);

    List<Pet> findPetsByMemberId(Long memberId);

    void deleteAllByMember(Member member);

}

package com.example.waggle.service.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.MemberRepository;
import com.example.waggle.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    @Transactional
    public PetDto addPet(PetDto petDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        // DB에 저장
        Pet pet = petRepository.save(petDto.toEntity());
        // 연관관계 설정
        pet.setMember(member);
        return PetDto.toDto(pet);
    }

    @Transactional
    public void removePet(Long petId) {

    }

}

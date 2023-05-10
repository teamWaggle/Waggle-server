package com.example.waggle.service.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.MemberRepository;
import com.example.waggle.repository.PetRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {
    @Autowired private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    @Transactional
    public PetDto addPet(PetDto petDto, MemberDto memberDto) {
        Member member = memberRepository.findById(memberDto.getId()).orElse(null);
        // DB에 저장
        Pet pet = petRepository.save(petDto.toEntity());
        // 연관관계 설정
        pet.setMember(member);
        return modelMapper.map(pet, PetDto.class);
    }

}

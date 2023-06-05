package com.example.waggle.repository.member;

import com.example.waggle.domain.member.Pet;
import com.example.waggle.domain.member.Sex;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.PetDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.member.PetService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetRepositoryTest {

    @Autowired
    PetRepository petRepository;

    @Autowired
    MemberService memberService;
    @Autowired
    PetService petService;

    private MemberDto savedMemberDto;
    private List<Pet> savedPetList = new ArrayList<>();

    @BeforeEach
    void init() {
        // member 저장
        SignUpDto signUpDto = SignUpDto.builder()
                .username("user")
                .password("12345678")
                .nickname("닉네임")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        savedMemberDto = memberService.signUp(signUpDto);

        // pet 저장
        PetDto petDto1 = PetDto.builder()
                .name("루이")
                .breed("포메라니안")
                .sex(Sex.MALE)
                .username(savedMemberDto.getUsername())
                .birthday(LocalDateTime.now()).build();

        PetDto petDto2 = PetDto.builder()
                .name("루이2")
                .breed("포메라니안2")
                .sex(Sex.MALE)
                .username(savedMemberDto.getUsername())
                .birthday(LocalDateTime.now()).build();

        Pet savedPet1 = petService.addPet(petDto1).toEntity(savedMemberDto.toEntity());
        Pet savedPet2 = petService.addPet(petDto2).toEntity(savedMemberDto.toEntity());
        savedPetList.add(savedPet1);
        savedPetList.add(savedPet2);
    }

    @Test
    @Transactional
    void findByMemberId() {
        List<Pet> pets = petRepository.findByMemberId(savedMemberDto.getId());

        assertThat(pets.size()).isEqualTo(2);
        assertThat(pets.get(0).getName()).isEqualTo("루이");
        assertThat(pets.get(1).getName()).isEqualTo("루이2");
    }

    @Test
    void findByUsername() {
        List<Pet> pets = petRepository.findByUsername(savedMemberDto.getUsername());

        assertThat(pets.size()).isEqualTo(2);
        assertThat(pets.get(0).getName()).isEqualTo("루이");
        assertThat(pets.get(1).getName()).isEqualTo("루이2");
    }
}
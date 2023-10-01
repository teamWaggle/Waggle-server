package com.example.waggle.repository.member;

import com.example.waggle.member.domain.Gender;
import com.example.waggle.pet.domain.Pet;
import com.example.waggle.member.dto.MemberDto;
import com.example.waggle.pet.dto.PetDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.pet.repository.PetRepository;
import com.example.waggle.member.service.MemberService;
import com.example.waggle.pet.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                .gender(Gender.MALE)
                .username(savedMemberDto.getUsername())
                .birthday(LocalDate.now()).build();

        PetDto petDto2 = PetDto.builder()
                .name("루이2")
                .breed("포메라니안2")
                .gender(Gender.MALE)
                .username(savedMemberDto.getUsername())
                .birthday(LocalDate.now()).build();

        System.out.println("petDto1 = " + petDto1);
        System.out.println("petDto2 = " + petDto2);

        Pet savedPet1 = petService.addPet(petDto1).toEntity(savedMemberDto.toEntity());
        Pet savedPet2 = petService.addPet(petDto2).toEntity(savedMemberDto.toEntity());
        savedPetList.add(savedPet1);
        savedPetList.add(savedPet2);
    }


}
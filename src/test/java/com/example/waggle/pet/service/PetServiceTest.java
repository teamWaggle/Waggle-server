package com.example.waggle.pet.service;

import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.exception.CustomAlertException;
import com.example.waggle.domain.member.domain.Gender;
import com.example.waggle.domain.member.dto.MemberSummaryDto;
import com.example.waggle.domain.member.dto.SignUpDto;
import com.example.waggle.domain.pet.dto.PetDto;
import com.example.waggle.domain.pet.repository.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class PetServiceTest {
    @Autowired DatabaseCleanUp databaseCleanUp;
    @Autowired PetRepository petRepository;

    @Autowired
    MemberService memberService;
    @Autowired
    PetService petService;

    private MemberSummaryDto memberSummaryDto;
    private Long savedPetId;

    @BeforeEach
    void beforeEach() {
        // member 저장
        SignUpDto signUpDto = SignUpDto.builder()
                .username("user")
                .password("12345678")
                .nickname("닉네임")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        memberSummaryDto = memberService.signUp(signUpDto, null);

        // pet 저장
        PetDto petDto = PetDto.builder()
                .name("루이")
                .breed("포메라니안")
                .gender(Gender.MALE)
                .username(memberSummaryDto.getUsername())
                .birthday(LocalDate.now()).build();

        savedPetId = petService.createPet(petDto);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    void findByPetId() {
        // petId로 조회
        PetDto findPet = petService.getPetById(savedPetId);
        assertThat(savedPetId).isEqualTo(findPet.getId());
    }

    @Test
    void updatePet() {
        // pet 수정 (변경 사항만 수정하는 건 컨트롤러 계층에서 처리)
        PetDto updatePetDto = PetDto.builder()
                .name("루이2")
                .breed("포메라니안2")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(2016, 9, 17)).build();

        Long updatedPetId = petService.updatePet(savedPetId, updatePetDto);
        PetDto updatedPetDto = petService.getPetById(updatedPetId);
        assertThat(updatedPetDto.getName()).isEqualTo(updatePetDto.getName());

        // repository에서 member 조회시 펫 수정 확인
//        List<PetDto> petDtoList = petService.findByUsername(savedMemberDto.getUsername());
//        log.info(petDtoList.get(0).getName());
    }

    @Test
    void removePet() {
        // pet 삭제
        petService.deletePet(savedPetId);

        Assertions.assertThrows(CustomAlertException.class, () -> petService.getPetById(savedPetId));

//        List<PetDto> tmp = petService.findByMemberId(savedMemberDto.getId());
//        assertThat(tmp.size()).isEqualTo(0);
    }
}
package com.example.waggle.service.member;

import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Sex;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.PetDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.repository.member.PetRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class PetServiceTest {
    @Autowired DatabaseCleanUp databaseCleanUp;
    @Autowired PetRepository petRepository;

    @Autowired MemberService memberService;
    @Autowired PetService petService;

    private MemberDto savedMemberDto;
    private PetDto savedPetDto;

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
        savedMemberDto = memberService.signUp(signUpDto);

        // pet 저장
        PetDto petDto = PetDto.builder()
                .name("루이")
                .breed("포메라니안")
                .sex(Sex.MALE)
                .username(savedMemberDto.getUsername())
                .birthday(LocalDateTime.now()).build();

        savedPetDto = petService.addPet(petDto);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    void findByPetId() {
        // petId로 조회
        PetDto findPet = petService.findByPetId(savedPetDto.getId()).get();
        assertThat(savedPetDto).usingRecursiveComparison().isEqualTo(findPet);  // 객체의 참조 값이 동등하지 않을 때 필드 값을 비교
    }

    @Test
    void findByMemberId() {
        // memberId로 조회
        List<PetDto> findPetDtos = petService.findByMemberId(savedMemberDto.getId());
        assertThat(findPetDtos).usingRecursiveFieldByFieldElementComparator().contains(savedPetDto);
    }

    @Test
    void findByUsername() {
        // username으로 조회
        List<PetDto> findPetDtos = petService.findByUsername(savedMemberDto.getUsername());
        assertThat(findPetDtos).usingRecursiveFieldByFieldElementComparator().contains(savedPetDto);

    }

    @Test
    void addPet() {
        List<PetDto> pets = petService.findByMemberId(savedMemberDto.getId());
        assertThat(pets.get(0)).usingRecursiveComparison().isEqualTo(savedPetDto);
        assertThat(savedPetDto.getName()).isEqualTo("루이");
    }

    @Test
    void updatePet() {
        // pet 수정 (변경 사항만 수정하는 건 컨트롤러 계층에서 처리)
        PetDto updatePetDto = PetDto.builder()
                .name("루이2")
                .breed("포메라니안2")
                .sex(Sex.MALE)
                .birthday(LocalDate.of(2016, 9, 17).atStartOfDay()).build();

        PetDto updatedPetDto = petService.updatePet(savedPetDto.getId(), updatePetDto);
        assertThat(updatedPetDto.getName()).isEqualTo(updatePetDto.getName());

        // repository에서 member 조회시 펫 수정 확인
        List<PetDto> petDtoList = petService.findByUsername(savedMemberDto.getUsername());
        log.info(petDtoList.get(0).getName());
    }

    @Test
    void removePet() {
        // pet 삭제
        petService.removePet(savedPetDto.getId());

        assertThat(petService.findByPetId(savedPetDto.getId())).isEmpty();

        List<PetDto> tmp = petService.findByMemberId(savedMemberDto.getId());
        assertThat(tmp.size()).isEqualTo(0);
    }
}
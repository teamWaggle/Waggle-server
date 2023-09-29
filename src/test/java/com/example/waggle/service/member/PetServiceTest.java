package com.example.waggle.service.member;

import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.domain.member.Gender;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.PetDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.repository.member.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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
                .gender(Gender.MALE)
                .username(savedMemberDto.getUsername())
                .birthday(LocalDate.now()).build();

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
    void updatePet() {
        // pet 수정 (변경 사항만 수정하는 건 컨트롤러 계층에서 처리)
        PetDto updatePetDto = PetDto.builder()
                .name("루이2")
                .breed("포메라니안2")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(2016, 9, 17)).build();

        PetDto updatedPetDto = petService.updatePet(savedPetDto.getId(), updatePetDto);
        assertThat(updatedPetDto.getName()).isEqualTo(updatePetDto.getName());

        // repository에서 member 조회시 펫 수정 확인
//        List<PetDto> petDtoList = petService.findByUsername(savedMemberDto.getUsername());
//        log.info(petDtoList.get(0).getName());
    }

    @Test
    void removePet() {
        // pet 삭제
        petService.removePet(savedPetDto.getId());

        assertThat(petService.findByPetId(savedPetDto.getId())).isEmpty();

//        List<PetDto> tmp = petService.findByMemberId(savedMemberDto.getId());
//        assertThat(tmp.size()).isEqualTo(0);
    }
}
package com.example.waggle.service.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Sex;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.PetDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Slf4j
class PetServiceTest {
    @Autowired MemberRepository memberRepository;

    @Autowired MemberService memberService;
    @Autowired PetService petService;

    private MemberDto savedMemberDto;
    private PetDto savedPetDto;

    @BeforeEach
    void beforeEach() {
        // member 저장
        SignUpDto signUpDto = SignUpDto.builder()
                .username("user1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        savedMemberDto = memberService.signUp(signUpDto);

        // pet 저장
        PetDto petDto = PetDto.builder()
                .name("루이")
                .breed("포메라니안")
                .sex(Sex.MALE)
                .birthday(LocalDateTime.now()).build();

        savedPetDto = petService.addPet(petDto, savedMemberDto.getId());
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
    @Transactional
    void addPet() {
        assertThat(savedMemberDto.getId()).isEqualTo(savedPetDto.getMember().getId());
        assertThat(savedPetDto.getName()).isEqualTo("루이");
    }

    @Test
    @Transactional
    void updatePet() {
        // pet 수정 (변경 사항만 수정하는 건 컨트롤러 계층에서 처리)
        PetDto updatePetDto = PetDto.builder()
                .name("루이2")
                .breed("포메라니안2")
                .sex(Sex.MALE)
                .birthday(LocalDateTime.now()).build();

        PetDto updatedPetDto = petService.updatePet(savedPetDto.getId(), updatePetDto);
        assertThat(updatedPetDto).usingRecursiveComparison().isEqualTo(updatedPetDto);

        // repository에서 member 조회시 펫 수정 확인
        Member tmp = memberRepository.findById(savedMemberDto.getId()).get();
        log.info(tmp.getPets().get(0).getName());
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void removePet() {
        // pet 삭제
        petService.removePet(savedPetDto.getId());

        assertThat(petService.findByPetId(savedPetDto.getId())).isEmpty();

        Member tmp = memberRepository.findById(savedMemberDto.getId()).get();
        assertThat(tmp.getPets().size()).isEqualTo(0);
    }
}
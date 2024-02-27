package com.example.waggle.pet.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.domain.pet.service.PetCommandService;
import com.example.waggle.domain.pet.service.PetQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.exception.handler.PetHandler;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import com.example.waggle.web.dto.pet.PetRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class PetServiceTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    PetRepository petRepository;

    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    PetCommandService petService;
    @Autowired
    PetQueryService petQueryService;


    private MemberSummaryDto memberSummaryDto;
    private Long savedPetId;
    Member member;


    @BeforeEach
    void beforeEach() {
        // member 저장
        MemberCredentialsDto signUpDto = MemberCredentialsDto.builder()
                .password("12345678")
                .email("dslkajflk")
                .build();
        Long memberId = memberCommandService.signUp(signUpDto);
        member = memberQueryService.getMemberById(memberId);

        // pet 저장
        PetRequest petDto = PetRequest.builder()
                .name("루이")
                .breed("포메라니안")
                .gender(Gender.MALE.toString())
                .age("now").build();

        savedPetId = petService.createPetByUsername(petDto, member.getUsername());
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    void findByPetId() {
        // petId로 조회
        Pet petById = petQueryService.getPetById(savedPetId);
        assertThat(savedPetId).isEqualTo(petById.getId());
    }

    @Test
    void findByUsername() {
        //given
        PetRequest build = PetRequest.builder()
                .name("hi")
                .gender(Gender.MALE.toString())
                .build();
        Long pet = petService.createPetByUsername(build, member.getUsername());
        //when
        List<Pet> petsByUsername = petQueryService.getPetsByUsername(member.getUsername());
        //then
        assertThat(petsByUsername.size()).isEqualTo(2);

    }

    @Test
    void updatePet() {
        // pet 수정 (변경 사항만 수정하는 건 컨트롤러 계층에서 처리)
        PetRequest updatePetDto = PetRequest.builder()
                .name("루이2")
                .breed("포메라니안2")
                .gender(Gender.MALE.toString())
                .age("now").build();

        Long updatedPetId = petService.updatePetByUsername(savedPetId, member.getUsername(), updatePetDto);
        Pet petById = petQueryService.getPetById(updatedPetId);
        assertThat(petById.getName()).isEqualTo(updatePetDto.getName());

        // repository에서 member 조회시 펫 수정 확인
//        List<PetDto> petDtoList = petService.findByUsername(savedMemberDto.getUsername());
//        log.info(petDtoList.get(0).getName());
    }

    @Test
    void removePet() {
        // pet 삭제
        petService.deletePetByUsername(savedPetId, member.getUsername());

        Assertions.assertThrows(PetHandler.class, () -> petQueryService.getPetById(savedPetId));

//        List<PetDto> tmp = petService.findByMemberId(savedMemberDto.getId());
//        assertThat(tmp.size()).isEqualTo(0);
    }

    @Test
    void remove_All_Pet_user_have() {
        //given
        PetRequest build = PetRequest.builder()
                .name("hi")
                .gender(Gender.MALE.toString())
                .build();
        Long pet = petService.createPetByUsername(build, member.getUsername());
        //when
        petService.deleteAllPetByUser(member.getUsername());
        //then
        List<Pet> petsByUsername = petQueryService.getPetsByUsername("member1");
        assertThat(petsByUsername.size()).isEqualTo(0);
    }
}
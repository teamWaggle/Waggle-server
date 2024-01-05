package com.example.waggle.member.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.pet.service.PetCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class MemberServiceImplTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberCommandService memberService;
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    PetCommandService petService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    int randomServerPort;


    private MemberRequest.RegisterDto signUpDto1;
    private MemberRequest.RegisterDto signUpDto2;
    private MemberRequest.RegisterDto signUpDto3;

    private MemberRequest.Put updateDto;


    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        signUpDto1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .email("asdklfj")
                .phone("010-1234-5678")
                .build();


        signUpDto2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .email("sdlfkjsalkfj")
                .phone("010-1234-5678")
                .build();

        signUpDto3 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임3")
                .address("서울시 광진구")
                .email("wjdgks")
                .phone("010-1234-5678")
                .build();

        updateDto = MemberRequest.Put.builder()
                .password("34567898765")
                .nickname("hann.o_i")
                .build();
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void signUp() {
        memberService.signUp(signUpDto1);

        Member member = memberQueryService.getMemberByUsername(signUpDto1.getUsername());

        assertThat(member.getUsername()).isEqualTo(signUpDto1.getUsername());
    }

    @Test
    public void 중복_회원_예외() {
        // savedMemberDto1와 savedMemberDto3의 username 중복 ➡︎ IllegalArgumentException 발생해야 함.
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);
        Assertions.assertThrows(MemberHandler.class, () -> memberService.signUp(signUpDto3));
    }

    @Test
    @Disabled
    public void signIn() {
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        MemberRequest.LoginDto signInDto = MemberRequest.LoginDto.builder()
                .username("member1")
                .password("12345678").build();

        // 로그인 요청

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        // API 요청 설정
        String url = "http://localhost:" + randomServerPort + "/member/test";
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
        log.info("responseEntity = {}", responseEntity);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(signInDto.getUsername());
    }

    @Test
    @WithMockCustomUser
    void member_Information_update() {
        //given
        Long aLong = memberService.signUp(signUpDto1);
        //when
        Long aLong1 = memberService.updateMemberInfo(updateDto);
        Member memberByUsername = memberQueryService.getMemberByUsername("member1");
        //then
        assertThat(memberByUsername.getNickname()).isEqualTo(updateDto.getNickname());
    }
}
//package com.example.waggle.member.service;
//
//import com.example.waggle.domain.member.entity.Member;
//import com.example.waggle.domain.member.repository.MemberRepository;
//import com.example.waggle.domain.member.service.MemberCommandService;
//import com.example.waggle.domain.pet.service.PetCommandService;
//import com.example.waggle.global.component.DatabaseCleanUp;
//import com.example.waggle.global.security.JwtToken;
//import com.example.waggle.web.dto.member.MemberRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Slf4j
//class MemberServiceImplTest {
//    @Autowired
//    DatabaseCleanUp databaseCleanUp;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    MemberCommandService memberService;
//    @Autowired
//    PetCommandService petService;
//    @Autowired
//    TestRestTemplate testRestTemplate;
//    @LocalServerPort
//    int randomServerPort;
//
//    private MemberRequest.RegisterRequestDto signUpDto1;
//    private MemberRequest.RegisterRequestDto signUpDto2;
//    private MemberRequest.RegisterRequestDto signUpDto3;
//
//
//    @BeforeEach
//    void beforeEach() {
//        // Member 회원가입
//        signUpDto1 = MemberRequest.RegisterRequestDto.builder()
//                .username("member1")
//                .password("12345678")
//                .nickname("닉네임1")
//                .address("서울시 광진구")
//                .phone("010-1234-5678")
//                .build();
//
//        signUpDto2 = MemberRequest.RegisterRequestDto.builder()
//                .username("member2")
//                .password("12345678")
//                .nickname("닉네임2")
//                .address("서울시 광진구")
//                .phone("010-1234-5678")
//                .build();
//
//        signUpDto3 = MemberRequest.RegisterRequestDto.builder()
//                .username("member1")
//                .password("12345678")
//                .nickname("닉네임3")
//                .address("서울시 광진구")
//                .phone("010-1234-5678")
//                .build();
//    }
//
//    @AfterEach
//    void afterEach() {
//        databaseCleanUp.truncateAllEntity();
//    }
//
//    @Test
//    public void signUp() {
//        Member member = memberService.signUp(signUpDto1, null);
//        Member member1 = memberService.signUp(signUpDto2, null);
//
//        assertThat(member.getUsername()).isEqualTo(signUpDto1.getUsername());
//        assertThat(member1.getUsername()).isEqualTo(signUpDto2.getUsername());
//    }
//
//    @Test
//    @Disabled
//    public void 중복_회원_예외() {
//        // savedMemberDto1와 savedMemberDto3의 username 중복 ➡︎ IllegalArgumentException 발생해야 함.
//        memberService.signUp(signUpDto1, null);
//        memberService.signUp(signUpDto2, null);
//        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService.signUp(signUpDto3, null));
//    }
//
//    @Test
//    @Disabled
//    public void signIn() {
//        memberService.signUp(signUpDto1, null);
//        memberService.signUp(signUpDto2, null);
//
//        MemberRequest.LoginRequestDto signInDto = MemberRequest.LoginRequestDto.builder()
//                .username("member1")
//                .password("12345678").build();
//
//        // 로그인 요청
//        JwtToken jwtToken = memberService.signIn(signInDto);
//
//        // HttpHeaders 객체 생성 및 토큰 추가
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//
//
//        // API 요청 설정
//        String url = "http://localhost:" + randomServerPort + "/member/test";
//        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
//        log.info("responseEntity = {}", responseEntity);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isEqualTo(signInDto.getUsername());
//    }
//}
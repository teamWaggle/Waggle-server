package com.example.waggle.service.member;

import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class MemberServiceImplTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    PetService petService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    int randomServerPort;

    private SignUpDto signUpDto1;
    private SignUpDto signUpDto2;
    private SignUpDto signUpDto3;


    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto3 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임3")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void signUp() {
        MemberDto savedMemberDto1 = memberService.signUp(signUpDto1);
        MemberDto savedMemberDto2 = memberService.signUp(signUpDto2);

        assertThat(savedMemberDto1.getUsername()).isEqualTo(signUpDto1.getUsername());
        assertThat(savedMemberDto2.getUsername()).isEqualTo(signUpDto2.getUsername());
    }

    @Test
    public void 중복_회원_예외() {
        // savedMemberDto1와 savedMemberDto3의 username 중복 ➡︎ IllegalArgumentException 발생해야 함.
        MemberDto savedMemberDto1 = memberService.signUp(signUpDto1);
        MemberDto savedMemberDto2 = memberService.signUp(signUpDto2);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> memberService.signUp(signUpDto3));
    }

    @Test
    @Disabled
    public void signIn() {
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        SignInDto signInDto = SignInDto.builder()
                .username("member1")
                .password("12345678").build();

        // 로그인 요청
        JwtToken jwtToken = memberService.signIn(signInDto);

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
}
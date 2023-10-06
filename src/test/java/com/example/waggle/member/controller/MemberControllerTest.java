package com.example.waggle.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class MemberControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @LocalServerPort
    int randomServerPort;

    @Test
    public void signUpTest() throws Exception {
        // header
//        String url = "http://localhost:" + randomServerPort + "/member/sign-up";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // body
//        SignUpDto signUpDto = SignUpDto.builder()
//                .username("test")
//                .password("12345678")
//                .phone("01012345678")
//                .nickname("test")
//                .build();
//        String body = objectMapper.writeValueAsString(signUpDto);
//
//        // API 요청 설정
//        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isEqualTo(signUpDto.getUsername());
    }


    @Test
    public void signIn() {

//        SignInDto signInDto = SignInDto.builder()
//                .username("member1")
//                .password("12345678").build();
//
//
//        // HttpHeaders 객체 생성 및 토큰 추가
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//
//        log.info("httpHeaders = {}", httpHeaders);
//
//        // API 요청 설정
//        String url = "http://localhost:" + randomServerPort + "/members/test";
//        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isEqualTo(signInDto.getUsername());
//
////        assertThat(SecurityUtil.getCurrentUsername()).isEqualTo(signInDto.getUsername()); // -> 테스트 코드에서는 인증을 위한 절차를 거치지 X. SecurityContextHolder 에 인증 정보가 존재하지 않는다.
//

    }
}

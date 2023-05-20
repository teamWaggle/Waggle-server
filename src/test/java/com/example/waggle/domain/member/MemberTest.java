package com.example.waggle.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Slf4j
class MemberTest {

    @Test
    void builderDefaultTest() {
        Member member = Member.builder().username("user").password("12345678").build();
        log.info("member = {}", member);
        assertThat(member).isNotNull();
    }

}
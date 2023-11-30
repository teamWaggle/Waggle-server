package com.example.waggle.board.helpU.service;

import com.example.waggle.domain.board.helpU.dto.HelpUDetailDto;
import com.example.waggle.domain.board.helpU.dto.HelpUSummaryDto;
import com.example.waggle.domain.board.helpU.dto.HelpUWriteDto;
import com.example.waggle.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.domain.board.helpU.service.HelpUService;
import com.example.waggle.domain.member.domain.Gender;
import com.example.waggle.domain.member.dto.SignUpDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class HelpUServiceTest {

    @Autowired
    private HelpUService helpUService;
    @Autowired
    private MemberService memberService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    HelpUWriteDto hwd1;
    HelpUWriteDto hwd2;
    HelpUWriteDto hwd3;
    HelpUWriteDto hwd4;

    SignUpDto signUpDto1;


    void setting() {
        hwd1 = HelpUWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("i")
                .petGender(Gender.MALE)
                .build();
        hwd2 = HelpUWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("ii")
                .petGender(Gender.MALE)
                .build();
        hwd3 = HelpUWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("iii")
                .petGender(Gender.MALE)
                .build();
        hwd4 = HelpUWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("iiii")
                .petGender(Gender.MALE)
                .build();
        signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    @WithMockCustomUser
    void helpU_create_service() throws IOException{
        setting();
        memberService.signUp(signUpDto1,null);
        Long helpUId = helpUService.createHelpU(hwd1,new ArrayList<>(),null);

        List<HelpUSummaryDto> allHelpU = helpUService.getAllHelpU();
        assertThat(allHelpU.size()).isEqualTo(1);
    }
    @Test
    @WithMockCustomUser
    void helpU_read_All_ByPaging_service() throws IOException {
        setting();
        memberService.signUp(signUpDto1,null);
        Long helpUId = helpUService.createHelpU(hwd1,new ArrayList<>(),null);
        helpUService.createHelpU(hwd2,new ArrayList<>(),null);
        helpUService.createHelpU(hwd3,new ArrayList<>(),null);
        helpUService.createHelpU(hwd4,new ArrayList<>(),null);

        //List<HelpUSummaryDto> allHelpU = helpUService.getAllHelpU();
        Pageable pageable = PageRequest.of(0, 3);
        Page<HelpUSummaryDto> allHelpUByPaging = helpUService.getPagedHelpUs(pageable);
        assertThat(allHelpUByPaging.getContent().size()).isEqualTo(3);
        assertThat(allHelpUByPaging.getContent().get(0).getPetName()).isEqualTo("i");
    }

    @Test
    @WithMockCustomUser
    void helpU_read_Mine_ByPaging_service() throws IOException {
        setting();
        memberService.signUp(signUpDto1,null);
        Long helpUId = helpUService.createHelpU(hwd1,new ArrayList<>(),null);
        helpUService.createHelpU(hwd2,new ArrayList<>(),null);
        helpUService.createHelpU(hwd3,new ArrayList<>(),null);
        helpUService.createHelpU(hwd4,new ArrayList<>(),null);

        //List<HelpUSummaryDto> allHelpU = helpUService.getAllHelpU();
        Pageable pageable = PageRequest.of(0, 2);
        Page<HelpUSummaryDto> allHelpUByPaging = helpUService.getPagedHelpUsByUsername("member1",pageable);
        assertThat(allHelpUByPaging.getContent().size()).isEqualTo(2);
        assertThat(allHelpUByPaging.getContent().get(1).getPetName()).isEqualTo("ii");
    }

    @Test
    @WithMockCustomUser
    void helpU_read_One_ByPaging_service() throws IOException{
        setting();
        memberService.signUp(signUpDto1,null);
        Long helpUId = helpUService.createHelpU(hwd1,new ArrayList<>(),null);
        helpUService.createHelpU(hwd2,new ArrayList<>(),null);
        helpUService.createHelpU(hwd3,new ArrayList<>(),null);
        helpUService.createHelpU(hwd4,new ArrayList<>(),null);

        HelpUDetailDto helpUByBoardId = helpUService.getHelpUByBoardId(helpUId);
        assertThat(helpUByBoardId.getContent()).isEqualTo("helpU page. hi");
    }
    @Test
    @WithMockCustomUser
    void helpU_update_One_ByPaging_service() throws IOException{
        setting();
        memberService.signUp(signUpDto1,null);
        helpUService.createHelpU(hwd1,new ArrayList<>(),null);
        helpUService.createHelpU(hwd2,new ArrayList<>(),null);
        Long helpU = helpUService.createHelpU(hwd3,new ArrayList<>(),null);

        Long aLong = helpUService.updateHelpU(helpU, hwd4,new ArrayList<>(),null);
        HelpUDetailDto helpUByBoardId = helpUService.getHelpUByBoardId(aLong);
        assertThat(helpUByBoardId.getPetName()).isEqualTo("iiii");
    }
    @Test
    @WithMockCustomUser
    void helpU_delete_One_service() throws IOException{
        setting();
        memberService.signUp(signUpDto1,null);
        helpUService.createHelpU(hwd1,new ArrayList<>(),null);
        helpUService.createHelpU(hwd2,new ArrayList<>(),null);
        Long helpU = helpUService.createHelpU(hwd3,new ArrayList<>(),null);
        helpUService.deleteHelpU(helpU);
        List<HelpUSummaryDto> allHelpU = helpUService.getAllHelpU();
        assertThat(allHelpU.size()).isEqualTo(2);
    }
}
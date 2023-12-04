package com.example.waggle.board.helpU.service;

import com.example.waggle.domain.board.help.service.HelpCommandService;
import com.example.waggle.domain.board.help.service.HelpQueryService;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.help.HelpDetailDto;
import com.example.waggle.web.dto.help.HelpSummaryDto;
import com.example.waggle.web.dto.help.HelpWriteDto;
import com.example.waggle.web.dto.member.MemberRequest;
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
class HelpServiceTest {

    @Autowired
    private HelpCommandService helpCommandService;
    @Autowired
    private HelpQueryService helpQueryService;
    @Autowired
    private MemberCommandService memberCommandService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    HelpWriteDto hwd1;
    HelpWriteDto hwd2;
    HelpWriteDto hwd3;
    HelpWriteDto hwd4;

    MemberRequest.RegisterRequestDto signUpDto1;


    void setting() {
        hwd1 = HelpWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("i")
                .petGender(Gender.MALE)
                .build();
        hwd2 = HelpWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("ii")
                .petGender(Gender.MALE)
                .build();
        hwd3 = HelpWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("iii")
                .petGender(Gender.MALE)
                .build();
        hwd4 = HelpWriteDto.builder()
                .content("helpU page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petName("iiii")
                .petGender(Gender.MALE)
                .build();
        signUpDto1 = MemberRequest.RegisterRequestDto.builder()
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
        memberCommandService.signUp(signUpDto1,null);
        Long helpUId = helpCommandService.createHelp(hwd1,new ArrayList<>(),null);

        List<HelpSummaryDto> allHelpU = helpQueryService.getAllHelp();
        assertThat(allHelpU.size()).isEqualTo(1);
    }
    @Test
    @WithMockCustomUser
    void helpU_read_All_ByPaging_service() throws IOException {
        setting();
        memberCommandService.signUp(signUpDto1,null);
        Long helpUId = helpCommandService.createHelp(hwd1,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd2,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd3,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd4,new ArrayList<>(),null);

        //List<HelpUSummaryDto> allHelpU = helpUService.getAllHelpU();
        Pageable pageable = PageRequest.of(0, 3);
        Page<HelpSummaryDto> allHelpUByPaging = helpQueryService.getPagedHelpList(pageable);
        assertThat(allHelpUByPaging.getContent().size()).isEqualTo(3);
        assertThat(allHelpUByPaging.getContent().get(0).getPetName()).isEqualTo("i");
    }

    @Test
    @WithMockCustomUser
    void helpU_read_Mine_ByPaging_service() throws IOException {
        setting();
        memberCommandService.signUp(signUpDto1,null);
        Long helpUId = helpCommandService.createHelp(hwd1,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd2,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd3,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd4,new ArrayList<>(),null);

        //List<HelpUSummaryDto> allHelpU = helpUService.getAllHelpU();
        Pageable pageable = PageRequest.of(0, 2);
        Page<HelpSummaryDto> allHelpUByPaging = helpQueryService.getPagedHelpListByUsername("member1",pageable);
        assertThat(allHelpUByPaging.getContent().size()).isEqualTo(2);
        assertThat(allHelpUByPaging.getContent().get(1).getPetName()).isEqualTo("ii");
    }

    @Test
    @WithMockCustomUser
    void helpU_read_One_ByPaging_service() throws IOException{
        setting();
        memberCommandService.signUp(signUpDto1,null);
        Long helpUId = helpCommandService.createHelp(hwd1,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd2,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd3,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd4,new ArrayList<>(),null);

        HelpDetailDto helpUByBoardId = helpQueryService.getHelpByBoardId(helpUId);
        assertThat(helpUByBoardId.getContent()).isEqualTo("helpU page. hi");
    }
    @Test
    @WithMockCustomUser
    void helpU_update_One_ByPaging_service() throws IOException{
        setting();
        memberCommandService.signUp(signUpDto1,null);
        helpCommandService.createHelp(hwd1,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd2,new ArrayList<>(),null);
        Long helpU = helpCommandService.createHelp(hwd3,new ArrayList<>(),null);

        Long aLong = helpCommandService.updateHelp(helpU, hwd4,new ArrayList<>(),null);
        HelpDetailDto helpUByBoardId = helpQueryService.getHelpByBoardId(aLong);
        assertThat(helpUByBoardId.getPetName()).isEqualTo("iiii");
    }
    @Test
    @WithMockCustomUser
    void helpU_delete_One_service() throws IOException{
        setting();
        memberCommandService.signUp(signUpDto1,null);
        helpCommandService.createHelp(hwd1,new ArrayList<>(),null);
        helpCommandService.createHelp(hwd2,new ArrayList<>(),null);
        Long helpU = helpCommandService.createHelp(hwd3,new ArrayList<>(),null);
        helpCommandService.deleteHelp(helpU);
        List<HelpSummaryDto> allHelpU = helpQueryService.getAllHelp();
        assertThat(allHelpU.size()).isEqualTo(2);
    }
}
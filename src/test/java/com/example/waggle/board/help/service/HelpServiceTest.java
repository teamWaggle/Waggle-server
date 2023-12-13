package com.example.waggle.board.help.service;

import com.example.waggle.domain.board.help.entity.Category;
import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.service.HelpCommandService;
import com.example.waggle.domain.board.help.service.HelpQueryService;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.help.HelpRequest;
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

    HelpRequest.Post hwd1;
    HelpRequest.Post hwd2;
    HelpRequest.Post hwd3;
    HelpRequest.Post hwd4;

    MemberRequest.RegisterRequestDto signUpDto1;


    void setting() {
        hwd1 = HelpRequest.Post.builder()
                .content("help page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(Category.FIND_PET)
                .build();
        hwd2 = HelpRequest.Post.builder()
                .content("help page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(Category.FIND_PET)
                .build();
        hwd3 = HelpRequest.Post.builder()
                .content("help page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(Category.FIND_PET)
                .build();
        hwd4 = HelpRequest.Post.builder()
                .content("help page4. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023,1,1,1,1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(Category.FIND_PET)
                .build();
        signUpDto1 = MemberRequest.RegisterRequestDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .email("wjdgks3264@naver.com")
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
    void help_create_service() throws IOException{
        setting();
        memberCommandService.signUp(signUpDto1);
        Long helpId = helpCommandService.createHelp(hwd1, null);

        List<Help> allHelp = helpQueryService.getAllHelp();
        assertThat(allHelp.size()).isEqualTo(1);
    }
    @Test
    @WithMockCustomUser
    void help_read_All_ByPaging_service() throws IOException {
        setting();
        memberCommandService.signUp(signUpDto1);
        Long helpId = helpCommandService.createHelp(hwd1,null);
        helpCommandService.createHelp(hwd2, null);
        helpCommandService.createHelp(hwd3, null);
        helpCommandService.createHelp(hwd4, null);

        //List<helpSummaryDto> allhelp = helpService.getAllhelp();
        Pageable pageable = PageRequest.of(0, 3);
        Page<Help> pagedHelpList = helpQueryService.getPagedHelpList(pageable);
        assertThat(pagedHelpList.getContent().size()).isEqualTo(3);
    }

    @Test
    @WithMockCustomUser
    void help_read_Mine_ByPaging_service() throws IOException {
        setting();
        memberCommandService.signUp(signUpDto1);
        Long helpId = helpCommandService.createHelp(hwd1, null);
        helpCommandService.createHelp(hwd2, null);
        helpCommandService.createHelp(hwd3, null);
        helpCommandService.createHelp(hwd4, null);

        //List<helpSummaryDto> allhelp = helpService.getAllhelp();
        Pageable pageable = PageRequest.of(0, 2);
        Page<Help> member1 = helpQueryService.getPagedHelpListByUsername("member1", pageable);
        assertThat(member1.getContent().size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void help_read_One_service() throws IOException{
        setting();
        memberCommandService.signUp(signUpDto1);
        Long helpId = helpCommandService.createHelp(hwd1, null);
        helpCommandService.createHelp(hwd2, null);
        helpCommandService.createHelp(hwd3, null);
        helpCommandService.createHelp(hwd4, null);

        Help help = helpQueryService.getHelpByBoardId(helpId);
        assertThat(help.getContent()).isEqualTo("help page. hi");
    }
    @Test
    @WithMockCustomUser
    void help_update_One_service() throws IOException{
        setting();
        memberCommandService.signUp(signUpDto1);
        Long helpId = helpCommandService.createHelp(hwd1, null);
        helpCommandService.createHelp(hwd2, null);
        helpCommandService.createHelp(hwd3, null);

        Long aLong = helpCommandService.updateHelp(helpId, hwd4, null, null);
        Help help = helpQueryService.getHelpByBoardId(aLong);
        assertThat(help.getContent()).isEqualTo("help page4. hi");
    }
    @Test
    @WithMockCustomUser
    void help_delete_One_service() throws IOException{
        setting();
        memberCommandService.signUp(signUpDto1);
        helpCommandService.createHelp(hwd1, null);
        helpCommandService.createHelp(hwd2, null);
        Long help = helpCommandService.createHelp(hwd3, null);
        helpCommandService.deleteHelp(help);
        List<Help> allHelp = helpQueryService.getAllHelp();
        assertThat(allHelp.size()).isEqualTo(2);
    }
}
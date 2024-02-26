package com.example.waggle.board.help.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.board.siren.service.SirenCommandService;
import com.example.waggle.domain.board.siren.service.SirenQueryService;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.member.MemberRequest.TemporaryRegisterDto;
import com.example.waggle.web.dto.siren.SirenRequest.SirenCreateDto;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@SpringBootTest
class HelpServiceTest {

    @Autowired
    private SirenCommandService sirenCommandService;
    @Autowired
    private SirenQueryService sirenQueryService;
    @Autowired
    private MemberCommandService memberCommandService;
    @Autowired
    private MemberQueryService memberQueryService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    SirenCreateDto hwd1;
    SirenCreateDto hwd2;
    SirenCreateDto hwd3;
    SirenCreateDto hwd4;

    TemporaryRegisterDto signUpDto1;


    void setting() {
        hwd1 = SirenCreateDto.builder()
                .title("this is title")
                .content("help page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023, 1, 1, 1, 1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(SirenCategory.FIND_PET)
                .build();
        hwd2 = SirenCreateDto.builder()
                .title("this is title")
                .content("help page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023, 1, 1, 1, 1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(SirenCategory.FIND_PET)
                .build();
        hwd3 = SirenCreateDto.builder()
                .title("this is title")
                .content("help page. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023, 1, 1, 1, 1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(SirenCategory.FIND_PET)
                .build();
        hwd4 = SirenCreateDto.builder()
                .title("this is title")
                .content("help page4. hi")
                .contact("01025522972")
                .lostDate(LocalDateTime.of(2023, 1, 1, 1, 1))
                .lostLocate("Seoul")
                .petGender(Gender.MALE)
                .category(SirenCategory.FIND_PET)
                .build();
        signUpDto1 = TemporaryRegisterDto.builder()
                .email("email1@naver.com")
                .password("password1")
                .build();
    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    void help_create_service() {
        setting();
        Long memberId = memberCommandService.signUp(signUpDto1);

        Member member = memberQueryService.getMemberById(memberId);
        Long helpId = sirenCommandService.createSirenByUsername(hwd1, null, member.getUsername());

        List<Siren> allHelp = sirenQueryService.getAllSiren();
        assertThat(allHelp.size()).isEqualTo(1);
    }

    @Test
    void help_read_All_ByPaging_service() {
        setting();
        Long memberId = memberCommandService.signUp(signUpDto1);
        Member member = memberQueryService.getMemberById(memberId);

        sirenCommandService.createSirenByUsername(hwd1, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd2, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd3, null, member.getUsername());

        //List<helpSummaryDto> allhelp = helpService.getAllhelp();
        Pageable pageable = PageRequest.of(0, 2);
        Page<Siren> pagedHelpList = sirenQueryService.getPagedSirenList(pageable);
        assertThat(pagedHelpList.getContent().size()).isEqualTo(2);
    }

    @Test
    void help_read_Mine_ByPaging_service() {
        setting();
        Long memberId = memberCommandService.signUp(signUpDto1);
        Member member = memberQueryService.getMemberById(memberId);

        sirenCommandService.createSirenByUsername(hwd1, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd2, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd3, null, member.getUsername());

        //List<helpSummaryDto> allhelp = helpService.getAllhelp();
        Pageable pageable = PageRequest.of(0, 2);
        Page<Siren> member1 = sirenQueryService.getPagedSirenListByMemberId(memberId, pageable);
        assertThat(member1.getContent().size()).isEqualTo(2);
    }

    @Test
    void help_read_One_service() throws IOException {
        setting();
        Long memberId = memberCommandService.signUp(signUpDto1);
        Member member = memberQueryService.getMemberById(memberId);

        Long helpId = sirenCommandService.createSirenByUsername(hwd1, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd2, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd3, null, member.getUsername());

        Siren siren = sirenQueryService.getSirenByBoardId(helpId);
        assertThat(siren.getContent()).isEqualTo("help page. hi");
    }

    @Test
    void help_update_One_service() {
        setting();
        Long memberId = memberCommandService.signUp(signUpDto1);
        Member member = memberQueryService.getMemberById(memberId);

        Long helpId = sirenCommandService.createSirenByUsername(hwd1, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd2, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd3, null, member.getUsername());

        Long aLong = sirenCommandService.updateSirenByUsername(helpId, member.getUsername(), hwd4, null, null);
        Siren help = sirenQueryService.getSirenByBoardId(aLong);
        assertThat(help.getContent()).isEqualTo("help page4. hi");
    }

    @Test
    void help_delete_One_service() throws IOException {
        setting();
        Long memberId = memberCommandService.signUp(signUpDto1);
        Member member = memberQueryService.getMemberById(memberId);

        sirenCommandService.createSirenByUsername(hwd1, null, member.getUsername());
        sirenCommandService.createSirenByUsername(hwd2, null, member.getUsername());
        Long help = sirenCommandService.createSirenByUsername(hwd3, null, member.getUsername());

        sirenCommandService.deleteSirenByUsername(help, member.getUsername());
        List<Siren> allHelp = sirenQueryService.getAllSiren();
        assertThat(allHelp.size()).isEqualTo(2);
    }
}
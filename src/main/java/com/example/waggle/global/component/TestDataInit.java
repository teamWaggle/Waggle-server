//package com.example.waggle.global.component;
//
//import com.example.waggle.domain.board.help.service.HelpService;
//import com.example.waggle.domain.board.persistence.entity.Story;
//import com.example.waggle.domain.board.persistence.dao.story.jpa.StoryRepository;
//import com.example.waggle.domain.member.entity.Member;
//import com.example.waggle.domain.member.repository.MemberRepository;
//import com.example.waggle.domain.member.application.MemberCommandService;
//import com.example.waggle.domain.schedule.service.schedule.ScheduleCommandService;
//import com.example.waggle.domain.schedule.service.team.TeamCommandService;
//import com.example.waggle.web.dto.help.HelpWriteDto;
//import com.example.waggle.web.dto.member.MemberSummaryDto;
//import com.example.waggle.web.dto.member.SignUpDto;
//import com.example.waggle.web.dto.schedule.ScheduleDto;
//import com.example.waggle.web.dto.schedule.TeamDto;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class TestDataInit {
//    private final MemberCommandService memberCommandService;
//    private final TeamCommandService teamCommandService;
//    private final ScheduleCommandService scheduleCommandService;
//    private final HelpService helpUService;
//
//    private final StoryRepository storyRepository;
//    private final MemberRepository memberRepository;
//
//    List<Member> members = new ArrayList<>();
//
//    @PostConstruct
//    public void init() {
////        initMember();
////        initHelpU();
////        initStory();
////        initTeamAndSchedule();
//    }
//
//    public void initMember() {
//        for (int i = 1; i <= 5; i++) {
//            SignUpDto signUpDto = SignUpDto.builder()
//                    .nickname("nickname" + i)
//                    .username("user" + i)
//                    .password("12345678")
//                    .phone("01011112222")
//                    .build();
//            MemberSummaryDto memberSummaryDto = memberCommandService.signUp(signUpDto, null);
//            //Member member = memberRepository.findByUsername(memberSummaryDto.getUsername()).get();
//            //members.add(member);
//        }
//    }
//
//    public void initStory() {
//        for (int i = 1; i <= 5; i++) {
//            Story story = Story.builder()
//                    .member(members.get(i - 1))
//                    .content("content" + i)
//                    .thumbnail("puppy" + i + ".png")
//                    .build();
//            storyRepository.save(story);
//        }
//    }
//
//    public void initTeamAndSchedule() {
//        String username = "user1";
//
//        Long team1 = teamCommandService.createTeam(TeamDto.builder().name("team1").build(), username);
//        Long team2 = teamCommandService.createTeam(TeamDto.builder().name("team2").build(), username);
//        teamCommandService.addMember(team1, "user2");
//        teamCommandService.addMember(team1, "user3");
//
//        scheduleCommandService.createSchedule(ScheduleDto.builder().title("산책").description("뚝섬한강공원").scheduleTime(LocalDateTime.now()).build(), team1);
//        scheduleCommandService.createSchedule(ScheduleDto.builder().title("애견카페").scheduleTime(LocalDateTime.now()).build(), team2);
//    }
//
//    public void initHelpU() {
//        List<HelpWriteDto> helps = new ArrayList<>();
//        helps.add(HelpWriteDto.builder().username("user1").content("1").title("2").petName("3").build());
//        helps.add(HelpWriteDto.builder().username("user1").content("2").title("2").petName("3").build());
//        helps.add(HelpWriteDto.builder().username("user1").content("3").title("2").petName("3").build());
//        helps.add(HelpWriteDto.builder().username("user1").content("4").title("2").petName("3").build());
//        helps.add(HelpWriteDto.builder().username("user1").content("5").title("2").petName("3").build());
//        helps.add(HelpWriteDto.builder().username("user1").content("6").title("2").petName("3").build());
//
//        for (HelpWriteDto help : helps) {
//            Long helpU = helpUService.createHelpTest(help,"user1");
//        }
//    }
//
//
//}

package com.example.waggle.commons.component;

import com.example.waggle.board.helpU.dto.HelpUWriteDto;
import com.example.waggle.board.helpU.service.HelpUService;
import com.example.waggle.board.story.domain.Story;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.member.service.MemberCommandService;
import com.example.waggle.schedule.dto.ScheduleDto;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.schedule.service.ScheduleService;
import com.example.waggle.schedule.service.TeamService;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {
    private final MemberCommandService memberCommandService;
    private final TeamService teamService;
    private final ScheduleService scheduleService;
    private final HelpUService helpUService;

    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;

    List<Member> members = new ArrayList<>();

    @PostConstruct
    public void init() {
//        initMember();
//        initHelpU();
//        initStory();
//        initTeamAndSchedule();
    }

    public void initMember() {
        for (int i = 1; i <= 5; i++) {
            SignUpDto signUpDto = SignUpDto.builder()
                    .nickname("nickname" + i)
                    .username("user" + i)
                    .password("12345678")
                    .phone("01011112222")
                    .build();
            MemberSummaryDto memberSummaryDto = memberCommandService.signUp(signUpDto, null);
            //Member member = memberRepository.findByUsername(memberSummaryDto.getUsername()).get();
            //members.add(member);
        }
    }

    public void initStory() {
        for (int i = 1; i <= 5; i++) {
            Story story = Story.builder()
                    .member(members.get(i - 1))
                    .content("content" + i)
                    .thumbnail("puppy" + i + ".png")
                    .build();
            storyRepository.save(story);
        }
    }

    public void initTeamAndSchedule() {
        String username = "user1";

        Long team1 = teamService.createTeam(TeamDto.builder().name("team1").build(), username);
        Long team2 = teamService.createTeam(TeamDto.builder().name("team2").build(), username);
        teamService.addMember(team1, "user2");
        teamService.addMember(team1, "user3");

        scheduleService.createSchedule(ScheduleDto.builder().title("산책").description("뚝섬한강공원").scheduleTime(LocalDateTime.now()).build(), team1);
        scheduleService.createSchedule(ScheduleDto.builder().title("애견카페").scheduleTime(LocalDateTime.now()).build(), team2);
    }

    public void initHelpU() {
        List<HelpUWriteDto> helps = new ArrayList<>();
        helps.add(HelpUWriteDto.builder().username("user1").content("1").title("2").petName("3").build());
        helps.add(HelpUWriteDto.builder().username("user1").content("2").title("2").petName("3").build());
        helps.add(HelpUWriteDto.builder().username("user1").content("3").title("2").petName("3").build());
        helps.add(HelpUWriteDto.builder().username("user1").content("4").title("2").petName("3").build());
        helps.add(HelpUWriteDto.builder().username("user1").content("5").title("2").petName("3").build());
        helps.add(HelpUWriteDto.builder().username("user1").content("6").title("2").petName("3").build());

        for (HelpUWriteDto help : helps) {
            Long helpU = helpUService.createHelpUTest(help,"user1");
        }
    }


}

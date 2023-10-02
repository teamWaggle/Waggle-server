package com.example.waggle.commons.component;

import com.example.waggle.board.story.domain.Story;

import com.example.waggle.member.domain.Member;
import com.example.waggle.member.dto.MemberDetailDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.schedule.dto.ScheduleDto;
import com.example.waggle.schedule.dto.TeamDto;

import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.member.repository.MemberRepository;

import com.example.waggle.member.service.MemberService;
import com.example.waggle.schedule.service.ScheduleService;
import com.example.waggle.schedule.service.TeamService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {
    private final MemberService memberService;
    private final TeamService teamService;
    private final ScheduleService scheduleService;

    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;

    List<Member> members = new ArrayList<>();

    @PostConstruct
    public void init() {
//        initMember();
//        initStory();
//        initTeamAndSchedule();
    }

    public void initMember() {
        for (int i = 1; i <= 5; i++) {
            SignUpDto signUpDto = SignUpDto.builder()
                    .nickname("nickname" + i)
                    .username("user" + i)
                    .password("12345678")
                    .build();
            MemberDetailDto memberDetailDto = memberService.signUp(signUpDto, null);
            Member member = memberRepository.findByUsername(memberDetailDto.getUsername()).get();
            members.add(member);
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

        TeamDto team1 = teamService.createTeam(TeamDto.builder().name("team1").build(), username);
        TeamDto team2 = teamService.createTeam(TeamDto.builder().name("team2").build(), username);
        teamService.addMember(team1.getId(), "user2");
        teamService.addMember(team1.getId(), "user3");

        scheduleService.createSchedule(ScheduleDto.builder().title("산책").description("뚝섬한강공원").scheduleTime(LocalDateTime.now()).build(), team1.getId());
        scheduleService.createSchedule(ScheduleDto.builder().title("애견카페").scheduleTime(LocalDateTime.now()).build(), team2.getId());
    }


}

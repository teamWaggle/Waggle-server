package com.example.waggle.component;


import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;

import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.dto.team.ScheduleDto;
import com.example.waggle.dto.team.TeamDto;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import com.example.waggle.repository.board.comment.ReplyRepository;
import com.example.waggle.repository.member.MemberRepository;

import com.example.waggle.repository.team.TeamRepository;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.team.ScheduleService;
import com.example.waggle.service.team.TeamService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            MemberDto memberDto = memberService.signUp(signUpDto);
            Member member = memberRepository.findByUsername(memberDto.getUsername()).get();
            members.add(member);
        }
    }

    public void initStory() {
        for (int i = 1; i <= 5; i++) {
            Story story = Story.builder()
                    .member(members.get(i - 1))
                    .content("content" + i)
                    .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/438795ab-0623-438e-bf99-e89c92ff4b4f")
                    .build();
            storyRepository.save(story);
        }
    }

    public void initTeamAndSchedule() {
        String username = "user1";

        TeamDto team1 = teamService.createTeamWithMember(TeamDto.builder().name("team1").build(), username);
        TeamDto team2 = teamService.createTeamWithMember(TeamDto.builder().name("team2").build(), username);
        teamService.addMember(team1.getId(), "user2");
        teamService.addMember(team1.getId(), "user3");

        scheduleService.addSchedule(ScheduleDto.builder().title("산책").description("뚝섬한강공원").scheduleTime(LocalDateTime.now()).build(), team1.getId());
        scheduleService.addSchedule(ScheduleDto.builder().title("애견카페").scheduleTime(LocalDateTime.now()).build(), team2.getId());
    }


}

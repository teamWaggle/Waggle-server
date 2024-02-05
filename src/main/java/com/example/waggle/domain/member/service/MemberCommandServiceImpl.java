package com.example.waggle.domain.member.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.board.siren.service.SirenCommandService;
import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.domain.follow.repository.FollowRepository;
import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.domain.schedule.service.ScheduleCommandService;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.NameUtil;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.VerifyMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider.WAGGLE;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final RecommendRepository recommendRepository;
    private final FollowRepository followRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SirenRepository sirenRepository;
    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberQueryService memberQueryService;
    private final AwsS3Service awsS3Service;
    private final RedisService redisService;
    private final StoryCommandService storyCommandService;
    private final QuestionCommandService questionCommandService;
    private final AnswerCommandService answerCommandService;
    private final SirenCommandService sirenCommandService;
    private final ScheduleCommandService scheduleCommandService;
    //ELSE
    private final PasswordEncoder passwordEncoder;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";


    @Override
    public Long signUp(MemberRequest.SignUpDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member createdMember = Member.builder()
                .username(generateAutoUsername())
                .password(encodedPassword)
                .nickname(generateAutoNickname())
                .email(request.getEmail())
                .authProvider(WAGGLE)
                .role(Role.GUEST)
                .build();

        Member member = memberRepository.save(createdMember);

        return member.getId();
    }

    @Override
    public Long registerMemberInfo(String username, MemberRequest.RegisterDto request) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getRole() == Role.GUEST) {
            member.changeRole(Role.USER);
        }
        if (member.getProfileImgUrl() != null) {
            awsS3Service.deleteFile(member.getProfileImgUrl());
        }
        member.registerInfo(request);
        return member.getId();
    }


    @Override
    public Long updateMemberInfo(MemberRequest.Put request) {
        Member member = memberQueryService.getSignInMember();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //기존 프로필 존재 시 s3에서 삭제
        if (member.getProfileImgUrl() != null) {
            awsS3Service.deleteFile(member.getProfileImgUrl());
        }
        member.updateInfo(request, encodedPassword);
        return member.getId();
    }


    @Override
    public void deleteMember() {
        Member member = memberQueryService.getSignInMember();
        String username = member.getUsername();

        //TODO MemberMention DeleteAll
        replyRepository.deleteAllByMemberUsername(username);
        commentRepository.deleteAllByMemberUsername(username);
        followRepository.deleteAllByFromMemberUsername(username);
        followRepository.deleteAllByToMemberUsername(username);
        recommendRepository.deleteAllByMemberUsername(username);

        List<Story> stories = storyRepository.findListByMemberUsername(username);
        List<Question> questions = questionRepository.findListByMemberUsername(username);
        List<Answer> answers = answerRepository.findListByMemberUsername(username);
        List<Siren> sirens = sirenRepository.findListByMemberUsername(username);
        List<Schedule> schedules = scheduleRepository.findListByMemberUsername(username);

        stories.forEach(story -> storyCommandService.deleteStory(story.getId()));
        questions.forEach(question -> questionCommandService.deleteQuestion(question.getId()));
        answers.forEach(answer -> answerCommandService.deleteAnswer(answer.getId()));
        sirens.forEach(siren -> sirenCommandService.deleteSiren(siren.getId()));
        schedules.forEach(schedule -> scheduleCommandService.deleteSchedule(schedule.getId()));

        teamMemberRepository.deleteAllByMemberUsername(username);
        List<Team> teamsByLeader = teamRepository.findTeamByLeader_Username(username);
        teamsByLeader.stream()
                .forEach(team -> {
                    team.getSchedules().stream()
                            .forEach(schedule -> {
                                scheduleCommandService.deleteScheduleForHardReset(schedule.getId());
                            });
                    teamRepository.deleteById(team.getId());
                });
        memberRepository.delete(member);
    }

    @Override
    public void verifyMail(VerifyMailRequest request) {
        String authNum = redisService.getValue(AUTH_CODE_PREFIX + request.getEmail());
        boolean isSuccess = authNum.equals(request.getAuthCode());
        if (!isSuccess) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);  // TODO 인증 실패 에러 코드
        }
    }

    private String generateAutoUsername() {
        String username;
        do {
            username = NameUtil.generateAutoUsername();
        } while (memberRepository.existsByUsername(username));
        return username;
    }

    private String generateAutoNickname() {
        String username;
        do {
            username = NameUtil.generateAutoNickname();
        } while (memberRepository.existsByUsername(username));
        return username;
    }
}

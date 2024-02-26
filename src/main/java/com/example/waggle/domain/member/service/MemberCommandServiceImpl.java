package com.example.waggle.domain.member.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.BoardRepository;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.conversation.repository.CommentRepository;
import com.example.waggle.domain.conversation.repository.ReplyRepository;
import com.example.waggle.domain.follow.repository.FollowRepository;
import com.example.waggle.domain.hashtag.repository.BoardHashtagRepository;
import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.mention.repository.MentionRepository;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.*;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.NameUtil;
import com.example.waggle.global.util.NameUtil.NameType;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.MemberRequest.MemberUpdateDto;
import com.example.waggle.web.dto.member.MemberRequest.TemporaryRegisterDto;
import com.example.waggle.web.dto.member.VerifyMailRequest.EmailVerificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider.WAGGLE;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandServiceImpl implements MemberCommandService {


    private final PetRepository petRepository;
    private final FollowRepository followRepository;
    private final ParticipationRepository participationRepository;
    private final RecommendRepository recommendRepository;
    private final MentionRepository mentionRepository;
    private final BoardHashtagRepository boardHashtagRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final ScheduleRepository scheduleRepository;
    private final AnswerRepository answerRepository;
    private final MemberScheduleRepository memberScheduleRepository;
    private final BoardRepository boardRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    private final MemberQueryService memberQueryService;
    private final AwsS3Service awsS3Service;
    private final RedisService redisService;
    private final MediaCommandService mediaCommandService;

    private final PasswordEncoder passwordEncoder;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";


    @Override
    public Long signUp(TemporaryRegisterDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member createdMember = Member.builder()
                .username(generateAutoUsername())
                .password(encodedPassword)
                .nickname(generateAutoNickname())
                .userUrl(generateAutoUserUrl())
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
    public Long updateMemberInfo(String username, MemberUpdateDto request) {
        Member member = memberQueryService.getMemberByUsername(username);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //기존 프로필 존재 시 s3에서 삭제
        if (member.getProfileImgUrl() != null) {
            awsS3Service.deleteFile(member.getProfileImgUrl());
        }
        member.updateInfo(request, encodedPassword);
        return member.getId();
    }

    @Override
    public Long updatePassword(Long memberId, String password) {
        Member member = memberQueryService.getMemberById(memberId);
        member.changePassword(passwordEncoder.encode(password));
        return member.getId();
    }

    @Override
    public Long verifyEmailForPasswordChange(EmailVerificationDto request) {
        verifyMail(request);
        return memberQueryService.getMemberByEmail(request.getEmail()).getId();
    }

    @Override
    public void deleteMember(Long memberId) {
        // TODO boardHashtag, teamMember, scheduleMember ➡️ 삭제 시 고아 객체 처리 필요 (참조 카운팅, 스케줄링・・・)
        Member member = memberQueryService.getMemberById(memberId);

        deleteAllDataLinkedToMember(member);
        deleteMemberContent(member);
        deleteMemberTeams(member);

        memberRepository.delete(member);
    }

    private void deleteAllDataLinkedToMember(Member member) {
        petRepository.deleteAllByMember(member);
        followRepository.deleteAllByToMember(member);
        followRepository.deleteAllByFromMember(member);
        participationRepository.deleteAllByMember(member);
        recommendRepository.deleteAllByMember(member);
        mentionRepository.deleteAllByMentionedNickname(member.getNickname());
    }

    private void deleteMemberContent(Member member) {
        deleteBoards(boardRepository.findAllByMember(member));
        deleteCommentsAndReplies(commentRepository.findByMember(member));
        deleteReplies(replyRepository.findByMember(member));
    }

    private void deleteBoards(List<Board> boards) {
        for (Board board : boards) {
            boardHashtagRepository.deleteAllByBoard(board);
            mediaCommandService.deleteMedia(board);
            recommendRepository.deleteAllByBoard(board);

            deleteCommentsAndReplies(commentRepository.findByBoard(board));

            if (board instanceof Question) {
                answerRepository.deleteAllByQuestion((Question) board);
            }

            if (board instanceof Schedule) {
                memberScheduleRepository.deleteAllBySchedule((Schedule) board);
            }

            boardRepository.delete(board);
        }
    }

    private void deleteCommentsAndReplies(List<Comment> comments) {
        for (Comment comment : comments) {
            mentionRepository.deleteAllByConversation(comment);
            deleteReplies(replyRepository.findByComment(comment));
            commentRepository.delete(comment);
        }
    }

    private void deleteReplies(List<Reply> replies) {
        for (Reply reply : replies) {
            mentionRepository.deleteAllByConversation(reply);
            replyRepository.delete(reply);
        }
    }

    private void deleteMemberTeams(Member member) {
        List<Team> ledTeams = teamRepository.findAllByLeader(member);
        for (Team team : ledTeams) {
            List<Schedule> schedules = scheduleRepository.findAllByTeam(team);
            deleteBoards(new ArrayList<>(schedules));
            teamMemberRepository.deleteAllByTeam(team);
            teamRepository.delete(team);
        }

        List<TeamMember> teamMemberships = teamMemberRepository.findAllByMember(member);
        teamMemberRepository.deleteAll(teamMemberships);
    }

    @Override
    public void verifyMail(EmailVerificationDto request) {
        String authNum = redisService.getValue(AUTH_CODE_PREFIX + request.getEmail());
        boolean isSuccess = authNum.equals(request.getAuthCode());
        if (!isSuccess) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);  // TODO 인증 실패 에러 코드
        }
    }

    private String generateAutoUsername() {
        String username;
        do {
            username = NameUtil.generateAuto(NameType.USERNAME);
        } while (memberRepository.existsByUsername(username));
        return username;
    }

    private String generateAutoNickname() {
        String username;
        do {
            username = NameUtil.generateAuto(NameType.NICKNAME);
        } while (memberRepository.existsByNickname(username));
        return username;
    }

    private String generateAutoUserUrl() {
        String username;
        do {
            username = NameUtil.generateAuto(NameType.USERURL);
        } while (memberRepository.existsByUserUrl(username));
        return username;
    }
}

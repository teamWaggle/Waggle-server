package com.example.waggle.domain.member.service;

import static com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider.WAGGLE;

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
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.mention.repository.MentionRepository;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ParticipationRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.NameUtil;
import com.example.waggle.global.util.NameUtil.NameType;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.VerifyMailRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandServiceImpl implements MemberCommandService {


    private final PetRepository petRepository;
    private final MentionRepository mentionRepository;
    private final FollowRepository followRepository;
    private final ParticipationRepository participationRepository;

    private final BoardHashtagRepository boardHashtagRepository;
    private final ScheduleRepository scheduleRepository;

    private final MediaRepository mediaRepository;
    private final MemberScheduleRepository memberScheduleRepository;

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final RecommendRepository recommendRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final BoardRepository boardRepository;
    private final AnswerRepository answerRepository;

    private final MemberQueryService memberQueryService;
    private final AwsS3Service awsS3Service;
    private final RedisService redisService;

    private final PasswordEncoder passwordEncoder;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";


    @Override
    public Long signUp(MemberRequest.AccessDto request) {
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
            mediaRepository.deleteAllByBoard(board);
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
        List<TeamMember> teamMemberships = teamMemberRepository.findAllByMember(member);
        for (TeamMember teamMember : teamMemberships) {
            Team team = teamMember.getTeam();

            if (team.getLeader().equals(member)) {
                List<Schedule> schedules = scheduleRepository.findAllByTeam(team);
                deleteBoards(new ArrayList<>(schedules));
                teamMemberRepository.deleteAllByTeam(team);
                teamRepository.delete(team);
            } else {
                teamMemberRepository.delete(teamMember);
            }
        }
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

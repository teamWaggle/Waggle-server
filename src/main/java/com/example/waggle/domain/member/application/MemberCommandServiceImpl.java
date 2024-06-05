package com.example.waggle.domain.member.application;

import static com.example.waggle.security.oauth.factory.OAuth2UserInfoFactory.AuthProvider.WAGGLE;

import com.example.waggle.domain.board.persistence.dao.board.jpa.BoardRepository;
import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.chat.persistence.dao.ChatRoomMemberRepository;
import com.example.waggle.domain.chat.persistence.dao.ChatRoomRepository;
import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.conversation.persistence.dao.reply.ReplyRepository;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.conversation.persistence.entity.Reply;
import com.example.waggle.domain.follow.persistence.dao.FollowRepository;
import com.example.waggle.domain.hashtag.persistence.dao.BoardHashtagRepository;
import com.example.waggle.domain.media.application.MediaCommandService;
import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberCredentialsDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberProfileDto;
import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberUpdateDto;
import com.example.waggle.domain.member.presentation.dto.VerifyMailRequest.EmailVerificationDto;
import com.example.waggle.domain.pet.persistence.dao.PetRepository;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.MemberScheduleRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.ParticipationRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.ScheduleRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.TeamMemberRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.TeamRepository;
import com.example.waggle.domain.schedule.persistence.entity.Schedule;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.domain.schedule.persistence.entity.TeamMember;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.aws.AwsS3Service;
import com.example.waggle.global.service.redis.RedisService;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.NameUtil;
import com.example.waggle.global.util.NameUtil.NameType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandServiceImpl implements MemberCommandService {


    private final PetRepository petRepository;
    private final FollowRepository followRepository;
    private final ParticipationRepository participationRepository;
    private final RecommendRepository recommendRepository;
    private final BoardHashtagRepository boardHashtagRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberScheduleRepository memberScheduleRepository;
    private final BoardRepository boardRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    private final MemberQueryService memberQueryService;
    private final RedisService redisService;
    private final MediaCommandService mediaCommandService;
    private final AwsS3Service awsS3Service;

    private final PasswordEncoder passwordEncoder;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";


    @Override
    public Long signUp(MemberCredentialsDto registerMemberRequest) {
        String encodedPassword = passwordEncoder.encode(registerMemberRequest.getPassword());

        Member createdMember = Member.builder()
                .username(generateAutoUsername())
                .password(encodedPassword)
                .nickname(generateAutoNickname())
                .userUrl(generateAutoUserUrl())
                .email(registerMemberRequest.getEmail())
                .authProvider(WAGGLE)
                .role(Role.GUEST)
                .build();

        Member member = memberRepository.save(createdMember);

        return member.getId();
    }

    @Override
    public Long initializeMemberProfile(MemberProfileDto memberProfileRequest,
                                        Member member) {
        // TODO GUEST 외의 role을 가진 사용자가 접근하면 throw exception
        if (member.getRole() == Role.GUEST) {
            member.changeRole(Role.USER);
        }
        member.registerInfo(memberProfileRequest);
        return member.getId();
    }

    @Override
    public Long updateMemberProfile(MemberUpdateDto updateMemberRequest,
                                    Member member) {
        if (MediaUtil.validateRemoveImgInS3(member.getProfileImgUrl(), updateMemberRequest.getMemberProfileImg())) {
            awsS3Service.deleteFile(member.getProfileImgUrl());
        }
        member.updateInfo(updateMemberRequest);
        return member.getId();
    }

    @Override
    public Long updatePassword(Long memberId, String password) {
        Member member = memberQueryService.getMemberById(memberId);
        member.changePassword(passwordEncoder.encode(password));
        return member.getId();
    }

    @Override
    public Long convertRole(Member member, Role to) {
        member.changeRole(to);
        return member.getId();
    }

    @Override
    public Long verifyEmailForPasswordChange(EmailVerificationDto verifyEmailRequest) {
        verifyMail(verifyEmailRequest);
        return memberQueryService.getMemberByEmail(verifyEmailRequest.getEmail()).getId();
    }

    @Override
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // TODO boardHashtag, teamMember, scheduleMember ➡️ 삭제 시 고아 객체 처리 필요 (참조 카운팅, 스케줄링・・・)

        deleteAllDataLinkedToMember(member);
        deleteMemberContent(member);
        deleteMemberTeams(member);

        memberRepository.delete(member);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void deleteDormantMember() {
        List<Member> memberList = memberRepository.findByRole(Role.DORMANT);
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        List<Board> allByMemberIn = boardRepository.findAllByMemberIn(memberList);
        allByMemberIn.forEach(mediaCommandService::deleteMedia);
        memberList.stream()
                .filter(member -> member.getLastModifiedDate().isBefore(today.minusDays(1)))
                .forEach(member -> deleteMember(member.getId()));
    }

    @Override
    public void deleteMemberByAdmin(Member member, Long memberId) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION);
        }
        boardRepository.findAllByMember(member)
                .forEach(mediaCommandService::deleteMedia);
        replyRepository.deleteAllByMemberId(memberId);
        commentRepository.deleteAllByMemberId(memberId);
        boardRepository.deleteBoardsWithRelationsByMemberId(memberId);
        teamRepository.deleteTeamWithRelationsByMemberId(memberId);
        chatRoomRepository.deleteAllByOwner(member);
        memberRepository.deleteMemberWithRelations(memberId);
    }

    private void deleteAllDataLinkedToMember(Member member) {
        petRepository.deleteAllByMember(member);
        followRepository.deleteAllByToMember(member);
        followRepository.deleteAllByFromMember(member);
        participationRepository.deleteAllByMember(member);
        recommendRepository.deleteAllByMember(member);
        chatRoomMemberRepository.deleteAllByMember(member);
        chatRoomRepository.deleteAllByOwner(member);
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

            if (board instanceof Schedule) {
                memberScheduleRepository.deleteAllBySchedule((Schedule) board);
            }

            boardRepository.delete(board);
        }
    }

    private void deleteCommentsAndReplies(List<Comment> comments) {
        for (Comment comment : comments) {
            deleteReplies(replyRepository.findByComment(comment));
            commentRepository.delete(comment);
        }
    }

    private void deleteReplies(List<Reply> replies) {
        for (Reply reply : replies) {
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
    public void verifyMail(EmailVerificationDto verifyEmailRequest) {
        String authNum = redisService.getValue(AUTH_CODE_PREFIX + verifyEmailRequest.getEmail());
        boolean isSuccess = authNum.equals(verifyEmailRequest.getAuthCode());
        if (!isSuccess) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);  // TODO 인증 실패 에러 코드
        }
    }

    private String generateAutoUsername() {
        String username;
        do {
            username = UUID.randomUUID().toString();
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

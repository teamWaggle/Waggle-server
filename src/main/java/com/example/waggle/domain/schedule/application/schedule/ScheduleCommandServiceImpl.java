package com.example.waggle.domain.schedule.application.schedule;

import com.example.waggle.domain.board.application.board.BoardService;
import com.example.waggle.domain.board.persistence.dao.board.jpa.BoardRepository;
import com.example.waggle.domain.conversation.application.comment.CommentCommandService;
import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.dao.schedule.jpa.MemberScheduleRepository;
import com.example.waggle.domain.schedule.persistence.dao.schedule.jpa.ScheduleRepository;
import com.example.waggle.domain.schedule.persistence.dao.team.jpa.TeamMemberRepository;
import com.example.waggle.domain.schedule.persistence.dao.team.jpa.TeamRepository;
import com.example.waggle.domain.schedule.persistence.entity.MemberSchedule;
import com.example.waggle.domain.schedule.persistence.entity.Schedule;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.domain.schedule.presentation.dto.schedule.ScheduleRequest;
import com.example.waggle.exception.object.handler.ScheduleHandler;
import com.example.waggle.exception.object.handler.TeamHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.ScheduleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waggle.domain.board.persistence.entity.BoardType.SCHEDULE;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberScheduleRepository memberScheduleRepository;
    private final CommentCommandService commentCommandService;
    private final BoardService boardService;


    @Override
    public Long createSchedule(Long teamId, ScheduleRequest createScheduleRequest, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        validateTeamMember(team, member);
        validateOrderOfStartAndEnd(createScheduleRequest);

        Schedule schedule = buildSchedule(createScheduleRequest, team, member);
        team.addSchedule(schedule);
        scheduleRepository.save(schedule);

        MemberSchedule memberSchedule = buildMemberSchedule(member, schedule);
        memberScheduleRepository.save(memberSchedule);

        return schedule.getId();
    }

    @Override
    public Long updateSchedule(Long scheduleId, ScheduleRequest updateScheduleRequest, Member member) {
        if (!boardService.validateMemberUseBoard(scheduleId, SCHEDULE, member)) {
            throw new ScheduleHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        validateOrderOfStartAndEnd(updateScheduleRequest);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        schedule.update(updateScheduleRequest);

        return schedule.getId();
    }

    @Override
    public void deleteSchedule(Long scheduleId, Member member) {
        if (!boardService.validateMemberUseBoard(scheduleId, SCHEDULE, member)) {
            throw new ScheduleHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        schedule.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));

        memberScheduleRepository.deleteAllByScheduleId(scheduleId);
        scheduleRepository.delete(schedule);
    }

    @Override
    public void deleteScheduleWithRelations(Long scheduleId, Member member) {
        if (!boardService.validateMemberUseBoard(scheduleId, SCHEDULE, member)) {
            throw new ScheduleHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        commentRepository.deleteCommentsWithRelationsByBoard(scheduleId);
        boardRepository.deleteBoardsWithRelations(SCHEDULE, scheduleId);
    }

    @Override
    public void deleteScheduleForHardReset(Long scheduleId) {
        scheduleRepository.findById(scheduleId).ifPresent(
                schedule -> {
                    schedule.getComments()
                            .forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
                    memberScheduleRepository.deleteAllByScheduleId(scheduleId);
                    scheduleRepository.delete(schedule);
                }
        );
    }

    @Override
    public Long addMemberSchedule(Long scheduleId, Member member) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        validateScheduleIsInYourTeam(schedule, member);
        validateExistMemberSchedule(scheduleId, member);

        MemberSchedule memberSchedule = buildMemberSchedule(member, schedule);
        memberScheduleRepository.save(memberSchedule);
        return memberSchedule.getId();
    }

    @Override
    public void deleteMemberSchedule(Long scheduleId, Member member) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));

        validateScheduleIsInYourTeam(schedule, member);

        memberScheduleRepository.deleteByMemberIdAndScheduleId(member.getId(), scheduleId);
    }

    private static void validateTeamMember(Team team, Member member) {
        boolean isWriterInTeam = team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getMember().equals(member));
        if (!isWriterInTeam) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_NOT_IN_TEAM);
        }
    }

    private void validateScheduleIsInYourTeam(Schedule schedule, Member member) {
        if (!teamMemberRepository.existsByMemberAndTeam(member, schedule.getTeam())) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_IN_YOUR_TEAM_SCHEDULE);
        }
    }

    private void validateExistMemberSchedule(Long scheduleId, Member member) {
        if (memberScheduleRepository.existsByMemberIdAndScheduleId(member.getId(), scheduleId)) {
            throw new ScheduleHandler(ErrorStatus.SCHEDULE_WAS_ALREADY_CHOSEN);
        }
    }

    private static void validateOrderOfStartAndEnd(ScheduleRequest scheduleRequest) {
        ScheduleUtil.validateSchedule(scheduleRequest.getStartDate(), scheduleRequest.getEndDate());
        ScheduleUtil.validateSchedule(
                ScheduleUtil.convertLocalTime(scheduleRequest.getStartTime()),
                ScheduleUtil.convertLocalTime(scheduleRequest.getEndTime())
        );
    }

    private Schedule buildSchedule(ScheduleRequest createScheduleRequest, Team team, Member member) {
        return Schedule.builder()
                .team(team)
                .title(createScheduleRequest.getTitle())
                .content(createScheduleRequest.getContent())
                .startDate(createScheduleRequest.getStartDate())
                .endDate(createScheduleRequest.getEndDate())
                .startTime(ScheduleUtil.convertLocalTime(createScheduleRequest.getStartTime()))
                .endTime(ScheduleUtil.convertLocalTime(createScheduleRequest.getEndTime()))
                .member(member)
                .build();
    }

    private MemberSchedule buildMemberSchedule(Member member, Schedule schedule) {
        return MemberSchedule.builder()
                .member(member)
                .schedule(schedule)
                .build();
    }
}

package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.entity.MemberSchedule;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.ScheduleUtil;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberScheduleRepository memberScheduleRepository;
    private final CommentCommandService commentCommandService;
    private final BoardService boardService;


    @Override
    public Long createSchedule(Long teamId, ScheduleRequest createScheduleRequest, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        validateTeamMember(team, member);
        ScheduleUtil.validateSchedule(createScheduleRequest.getStartTime(), createScheduleRequest.getEndTime());

        Schedule schedule = buildSchedule(createScheduleRequest, team, member);
        team.addSchedule(schedule);
        scheduleRepository.save(schedule);

        MemberSchedule memberSchedule = buildMemberSchedule(member, schedule);
        memberScheduleRepository.save(memberSchedule);

        return schedule.getId();
    }

    @Override
    public Long updateSchedule(Long scheduleId, ScheduleRequest updateScheduleRequest, Member member) {
        if (!boardService.validateMemberUseBoard(scheduleId, BoardType.SCHEDULE, member)) {
            throw new ScheduleHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        ScheduleUtil.validateSchedule(updateScheduleRequest.getStartTime(), updateScheduleRequest.getEndTime());

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        schedule.update(updateScheduleRequest);

        return schedule.getId();
    }

    @Override
    public void deleteSchedule(Long scheduleId, Member member) {
        if (!boardService.validateMemberUseBoard(scheduleId, BoardType.SCHEDULE, member)) {
            throw new ScheduleHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        schedule.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));

        memberScheduleRepository.deleteAllByScheduleId(scheduleId);
        scheduleRepository.delete(schedule);
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

    private Schedule buildSchedule(ScheduleRequest createScheduleRequest, Team team, Member member) {
        return Schedule.builder()
                .team(team)
                .title(createScheduleRequest.getTitle())
                .content(createScheduleRequest.getContent())
                .startTime(createScheduleRequest.getStartTime())
                .endTime(createScheduleRequest.getEndTime())
                .member(member)
                .build();
    }

    private static MemberSchedule buildMemberSchedule(Member member, Schedule schedule) {
        MemberSchedule memberSchedule = MemberSchedule.builder()
                .member(member)
                .schedule(schedule)
                .build();
        return memberSchedule;
    }
}

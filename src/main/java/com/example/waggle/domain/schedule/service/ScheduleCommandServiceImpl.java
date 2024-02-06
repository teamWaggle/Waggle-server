package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.schedule.ScheduleRequest.Post;
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
    private final CommentCommandService commentCommandService;
    private final BoardService boardService;


    @Override
    public Long createSchedule(Long teamId, Post request, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));


        validateTeamMember(team, member);

        Schedule createdSchedule = Schedule.builder()
                .team(team)
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        team.addSchedule(createdSchedule);

        Schedule schedule = scheduleRepository.save(createdSchedule);
        return schedule.getId();
    }

    @Override
    public Long updateSchedule(Long scheduleId, Post request) {
        if (!boardService.validateMemberUseBoard(scheduleId, BoardType.SCHEDULE)) {
            throw new ScheduleHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        schedule.update(request);

        return schedule.getId();
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        if (!boardService.validateMemberUseBoard(scheduleId, BoardType.SCHEDULE)) {
            throw new ScheduleHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        schedule.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        scheduleRepository.delete(schedule);
    }

    @Override
    public void deleteScheduleForHardReset(Long scheduleId) {
        scheduleRepository.findById(scheduleId).ifPresent(
                schedule -> {
                    schedule.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
                    scheduleRepository.delete(schedule);
                }
        );
    }

    private static void validateTeamMember(Team team, Member member) {
        boolean isWriterInTeam = team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getMember().equals(member));
        if (!isWriterInTeam) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_NOT_IN_TEAM);
        }
    }
}

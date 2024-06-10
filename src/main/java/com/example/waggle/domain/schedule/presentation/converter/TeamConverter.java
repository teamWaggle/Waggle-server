package com.example.waggle.domain.schedule.presentation.converter;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.domain.schedule.presentation.dto.team.Status;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamResponse.ParticipationStatusResponse;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamResponse.TeamDetailDto;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamResponse.TeamSummaryDto;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamResponse.TeamSummaryListDto;
import com.example.waggle.global.util.MediaUtil;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class TeamConverter {

    public static TeamDetailDto toDetailDto(Team team) {
        List<Member> teamMembers = team.getTeamMembers().stream()
                .map(teamMember -> teamMember.getMember()).collect(Collectors.toList());
        return TeamDetailDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .coverImageUrl(MediaUtil.getCoverImg(team))
                .teamColor(team.getTeamColor())
                .teamSize(team.getTeamMembers().size())
                .isPrivate(team.getIsPrivate())
                .teamLeader(MemberConverter.toMemberSummaryDto(team.getLeader()))
                .teamMemberList(
                        teamMembers.stream().map(MemberConverter::toMemberSummaryDto).collect(Collectors.toList()))
                .build();
    }

    public static TeamSummaryDto toSummaryDto(Team team) {
        return TeamSummaryDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .coverImageUrl(MediaUtil.getCoverImg(team))
                .description(team.getDescription())
                .teamColor(team.getTeamColor())
                .teamSize(team.getTeamMembers().size())
                .build();
    }


    public static TeamSummaryListDto toSummaryListDto(Page<Team> teamPage) {
        List<TeamSummaryDto> teamSummaryDtos = teamPage.stream()
                .map(TeamConverter::toSummaryDto)
                .collect(Collectors.toList());

        return TeamSummaryListDto.builder()
                .teamList(teamSummaryDtos)
                .teamCount(teamPage.getTotalElements())
                .isFirst(teamPage.isFirst())
                .isLast(teamPage.isLast())
                .build();
    }

    public static ParticipationStatusResponse toStatusDto(Status status) {
        return ParticipationStatusResponse.builder()
                .status(status)
                .build();
    }

}

package com.example.waggle.web.converter;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.schedule.TeamResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class TeamConverter {

    public static TeamResponse.DetailDto toDetailDto(Team team) {
        List<Member> teamMembers = team.getTeamMembers().stream()
                .map(teamMember -> teamMember.getMember()).collect(Collectors.toList());
        return TeamResponse.DetailDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .coverImageUrl(MediaUtil.getCoverImg(team))
                .colorScheme(team.getColorScheme())
                .maxTeamSize(team.getMaxTeamSize())
                .leader(MemberConverter.toMemberSummaryDto(team.getLeader()))
                .teamMember(teamMembers.stream().map(MemberConverter::toMemberSummaryDto).collect(Collectors.toList()))
                .build();
    }

    public static TeamResponse.SummaryDto toSummaryDto(Team team) {
        return TeamResponse.SummaryDto.builder()
                .name(team.getName())
                .coverImageUrl(MediaUtil.getCoverImg(team))
                .colorScheme(team.getColorScheme())
                .maxTeamSize(team.getMaxTeamSize())
                .build();
    }


    public static TeamResponse.ListDto toSummaryListDto(Page<Team> teamPage) {
        List<TeamResponse.SummaryDto> teamSummaryDtos = teamPage.stream()
                .map(TeamConverter::toSummaryDto)
                .collect(Collectors.toList());


        return TeamResponse.ListDto.builder()
                .teams(teamSummaryDtos)
                .totalQuestions(teamPage.getTotalElements())
                .isFirst(teamPage.isFirst())
                .isLast(teamPage.isLast())
                .build();
    }
}

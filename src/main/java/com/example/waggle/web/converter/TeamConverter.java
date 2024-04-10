package com.example.waggle.web.converter;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryListDto;
import com.example.waggle.web.dto.schedule.TeamResponse.TeamDetailDto;
import com.example.waggle.web.dto.schedule.TeamResponse.TeamSummaryDto;
import com.example.waggle.web.dto.schedule.TeamResponse.TeamSummaryListDto;
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
                .leader(MemberConverter.toMemberSummaryDto(team.getLeader()))
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

    public static MemberSummaryListDto toMemberSummaryListDto(List<Participation> participationList) {
        List<MemberSummaryDto> memberSummaryDtoList = participationList.stream()
                .map(participation -> MemberConverter.toMemberSummaryDto(participation.getMember()))
                .collect(Collectors.toList());
        return MemberSummaryListDto.builder()
                .memberList(memberSummaryDtoList)
                .memberCount(memberSummaryDtoList.size())
                .build();
    }

}

package com.example.waggle.schedule.service;

import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.schedule.dto.TeamDto;
import java.util.List;

public interface TeamQueryService {

    List<TeamDto> getTeams();

    List<TeamDto> getTeamsByUsername(String username);

    TeamDto getTeamById(Long teamId);

    List<MemberSummaryDto> getTeamMembers(Long teamId);

    boolean isTeamLeader(Long teamId, String username);

}

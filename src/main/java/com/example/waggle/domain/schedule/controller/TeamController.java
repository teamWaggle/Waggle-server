package com.example.waggle.domain.schedule.controller;

import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.domain.member.dto.MemberSummaryDto;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.dto.TeamDto;
import com.example.waggle.domain.schedule.service.TeamCommandService;
import com.example.waggle.domain.schedule.service.TeamQueryService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/team")
public class TeamController {
    private final MemberQueryService memberQueryService;
    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<MemberSummaryDto>> getTeamSchedules(@PathVariable Long teamId) {
        List<MemberSummaryDto> memberSummaryDtos = new ArrayList<>();

        TeamDto teamDto = teamQueryService.getTeamById(teamId);
        List<String> teamMembers = teamDto.getTeamMembers();
        for (String teamMember : teamMembers) {
            memberSummaryDtos.add(memberQueryService.getMemberSummaryDto(teamMember));
        }
        return ResponseEntity.ok(memberSummaryDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestParam String name) {
        TeamDto teamDto = TeamDto.builder().name(name).build();
        Long createdTeamDto = teamCommandService.createTeam(teamDto, SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok(createdTeamDto);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTeam(@RequestParam Long teamId, @RequestParam String name) {
        TeamDto teamDto = TeamDto.builder().name(name).build();
        Long updatedTeamDto = teamCommandService.updateTeam(teamId, teamDto);
        return ResponseEntity.ok(updatedTeamDto);
    }

    @PostMapping("/delete")
    public String deleteTeam(@ModelAttribute TeamDto teamDto) {
        teamCommandService.deleteTeam(teamDto.getId());
        return "redirect:/schedule";
    }

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        log.info("team/teams");
        List<TeamDto> teams = teamQueryService.getTeamsByUsername(SecurityUtil.getCurrentUsername());
        for (TeamDto team : teams) {
            log.info("team = {}", team);
        }
        return ResponseEntity.ok(teams);
    }


    @GetMapping("/checkTeamLeader")
    public ResponseEntity<Boolean> checkTeamLeader(@RequestParam Long teamId) {
        String username = SecurityUtil.getCurrentUsername();
        boolean isTeamLeader = teamQueryService.isTeamLeader(teamId, username);

        if (isTeamLeader) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }

    @PostMapping("/addMember")
    public ResponseEntity<String> addMember(@RequestParam Long teamId, @RequestParam String username) {
        teamCommandService.addMember(teamId, username);
        return ResponseEntity.ok("Team member added successfully");
    }

    @PostMapping("/removeMember")
    public ResponseEntity<String> removeMember(@RequestParam Long teamId, @RequestParam String username) {
        teamCommandService.removeMember(teamId, username);
        return ResponseEntity.ok("Team member removed successfully");
    }

}

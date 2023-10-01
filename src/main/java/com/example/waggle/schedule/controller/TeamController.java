package com.example.waggle.schedule.controller;

import com.example.waggle.commons.security.SecurityUtil;
import com.example.waggle.member.dto.MemberSimpleDto;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.member.service.MemberService;
import com.example.waggle.schedule.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/team")
public class TeamController {
    private final MemberService memberService;
    private final TeamService teamService;

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<MemberSimpleDto>> getTeamSchedules(@PathVariable Long teamId) {
        List<MemberSimpleDto> memberSimpleDtos = new ArrayList<>();

        Optional<TeamDto> byTeamId = teamService.findByTeamId(teamId);
        if(byTeamId.isPresent()) {
            TeamDto teamDto = byTeamId.get();
            List<String> teamMembers = teamDto.getTeamMembers();
            for (String teamMember : teamMembers) {
                memberSimpleDtos.add(memberService.findMemberSimpleDto(teamMember));
            }
        }
        return ResponseEntity.ok(memberSimpleDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestParam String name) {
        TeamDto teamDto = TeamDto.builder().name(name).build();
        TeamDto createdTeamDto = teamService.createTeamWithMember(teamDto, SecurityUtil.getCurrentUsername());
        if (createdTeamDto != null) {
            return ResponseEntity.ok(createdTeamDto);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create team");
        }
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateTeam(@RequestParam Long teamId, @RequestParam String name) {
        TeamDto teamDto = TeamDto.builder().name(name).build();
        TeamDto updatedTeamDto = teamService.updateTeam(teamId, teamDto);

        if (updatedTeamDto != null) {
            return ResponseEntity.ok(updatedTeamDto);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update team");
        }
    }

    @PostMapping("/delete")
    public String deleteTeam(@ModelAttribute TeamDto teamDto) {
        teamService.removeTeam(teamDto.getId());
        return "redirect:/schedule";
    }

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        log.info("team/teams");
        List<TeamDto> teams = teamService.findAllTeamByUsername(SecurityUtil.getCurrentUsername());
        for (TeamDto team : teams) {
            log.info("team = {}", team);
        }
        return ResponseEntity.ok(teams);
    }


    @GetMapping("/checkTeamLeader")
    public ResponseEntity<Boolean> checkTeamLeader(@RequestParam Long teamId) {
        String username = SecurityUtil.getCurrentUsername();
        boolean isTeamLeader = teamService.isTeamLeader(teamId, username);

        if (isTeamLeader) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }

    @PostMapping("/addMember")
    public ResponseEntity<String> addMember(@RequestParam Long teamId, @RequestParam String username) {
        TeamDto teamDto = teamService.addMember(teamId, username);
        if (teamDto != null) {
            return ResponseEntity.ok("Team member added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add team member");
        }
    }

    @PostMapping("/removeMember")
    public ResponseEntity<String> removeMember(@RequestParam Long teamId, @RequestParam String username) {
        Boolean isSuccess = teamService.removeMember(teamId, username);
        if (isSuccess) {
            return ResponseEntity.ok("Team member removed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove team member");
        }
    }

}

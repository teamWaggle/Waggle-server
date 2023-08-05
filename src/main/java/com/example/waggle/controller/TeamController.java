package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.dto.team.ScheduleDto;
import com.example.waggle.dto.team.TeamDto;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/create")
    public String createTeamForm(Model model) {
        return "schedule/team/addTeam";
    }

    @PostMapping("/create")
    public String createTeam(@ModelAttribute TeamDto teamDto) {
        TeamDto createdTeamDto = teamService.createTeamWithMember(teamDto, SecurityUtil.getCurrentUsername());
        return "redirect:/team/" + createdTeamDto.getId();
    }

    // update
    @GetMapping("/update")
    public String updateTeamForm(Model model) {

        return "team/updateTeam";
    }

    @PostMapping("/{teamId}/update")
    public String updateTeam(@PathVariable Long teamId, @ModelAttribute TeamDto teamDto) {
        TeamDto updatedTeamDto = teamService.updateTeam(teamId, teamDto);
        return "redirect:/team/" + updatedTeamDto.getId();
    }

    // delete
    @PostMapping("/delete")
    public String deleteTeam(@ModelAttribute TeamDto teamDto) {
        teamService.removeTeam(teamDto.getId());
        return "redirect:/team";
    }

}

package com.example.waggle.schedule.controller;

import com.example.waggle.commons.security.SecurityUtil;
import com.example.waggle.schedule.dto.ScheduleDto;
import com.example.waggle.team.dto.TeamDto;
import com.example.waggle.member.service.MemberService;
import com.example.waggle.schedule.service.ScheduleService;
import com.example.waggle.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final MemberService memberService;
    private final TeamService teamService;
    private final ScheduleService scheduleService;

    @GetMapping
    public String scheduleMain(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        List<TeamDto> allTeamByUsername = teamService.findAllTeamByUsername(username);
        Boolean isTeamLeader = Boolean.FALSE;
        if (allTeamByUsername.size() > 0) {
            isTeamLeader = teamService.isTeamLeader(allTeamByUsername.get(0).getId(), username);
        }

        model.addAttribute("currentUsername", username);
        model.addAttribute("teams", allTeamByUsername);
        model.addAttribute("isTeamLeader", isTeamLeader);
        return "schedule/schedule";
    }

    @GetMapping("/{teamId}/schedules")
    public ResponseEntity<List<ScheduleDto>> getTeamSchedules(@PathVariable Long teamId) {
        List<ScheduleDto> teamSchedules = scheduleService.findByTeamId(teamId);
        return ResponseEntity.ok(teamSchedules);
    }

    @PostMapping("/create")
    public String createSchedule(@ModelAttribute ScheduleDto scheduleDto, Model model) {
        scheduleService.addSchedule(scheduleDto, scheduleDto.getTeamId());
        return "redirect:/schedule";
    }

    @PostMapping("/update")
    public String updateSchedule(@ModelAttribute ScheduleDto scheduleDto, Model model) {
        scheduleService.updateSchedule(scheduleDto);
        return "redirect:/schedule";
    }

}
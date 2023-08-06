package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.team.Schedule;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.dto.team.ScheduleDto;
import com.example.waggle.dto.team.TeamDto;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.team.ScheduleService;
import com.example.waggle.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        model.addAttribute("teams", allTeamByUsername);
        return "schedule/scheduleMain";
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
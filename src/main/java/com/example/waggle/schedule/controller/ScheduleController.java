package com.example.waggle.schedule.controller;

import com.example.waggle.commons.security.SecurityUtil;
import com.example.waggle.schedule.dto.ScheduleDto;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.schedule.service.ScheduleCommandService;
import com.example.waggle.schedule.service.ScheduleQueryService;
import com.example.waggle.schedule.service.TeamCommandService;
import com.example.waggle.schedule.service.TeamQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;
    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;

    @GetMapping
    public String scheduleMain(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        List<TeamDto> allTeamByUsername = teamQueryService.getTeamsByUsername(username);
        Boolean isTeamLeader = Boolean.FALSE;
        if (allTeamByUsername.size() > 0) {
            isTeamLeader = teamQueryService.isTeamLeader(allTeamByUsername.get(0).getId(), username);
        }

        model.addAttribute("currentUsername", username);
        model.addAttribute("teams", allTeamByUsername);
        model.addAttribute("isTeamLeader", isTeamLeader);
        return "schedule/schedule";
    }

    @GetMapping("/{teamId}/schedules")
    public ResponseEntity<List<ScheduleDto>> getTeamSchedules(@PathVariable Long teamId) {
        List<ScheduleDto> teamSchedules = scheduleQueryService.getSchedulesByTeamId(teamId);
        return ResponseEntity.ok(teamSchedules);
    }

    @PostMapping("/create")
    public String createSchedule(@ModelAttribute ScheduleDto scheduleDto, Model model) {
        scheduleCommandService.createSchedule(scheduleDto, scheduleDto.getTeamId());
        return "redirect:/schedule";
    }

    @PostMapping("/update")
    public String updateSchedule(@ModelAttribute ScheduleDto scheduleDto, Model model) {
        scheduleCommandService.updateSchedule(scheduleDto);
        return "redirect:/schedule";
    }

}
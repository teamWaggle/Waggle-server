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

//        List<ScheduleDto> scheduleDtos = scheduleService.findByTeamId(1L);

        List<ScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleDtos.add(ScheduleDto.builder().id(1L).title("Event 1").scheduleTime(LocalDateTime.now()).build());

        model.addAttribute("teams", allTeamByUsername);
        model.addAttribute("scheduleDtos", scheduleDtos);

        return "schedule/scheduleMain";
    }

    @GetMapping("/{scheduleId}")
    public String schedule(@PathVariable Long scheduleId, Model model) {
        Optional<ScheduleDto> byScheduleId = scheduleService.findByScheduleId(scheduleId);
        if(byScheduleId.isPresent()) {
            ScheduleDto scheduleDto = byScheduleId.get();
            model.addAttribute("scheduleDto", scheduleDto);
            return "/schedule/schedule";
        } else {
            // TODO 예외처리
            return "redirect:/schedule";
        }
    }

    @GetMapping("/create")
    public String createScheduleForm(Model model) {

        return "schedule/scheduleForm";
    }

    @PostMapping("/create")
    public String createSchedule(@ModelAttribute ScheduleDto scheduleDto, Model model) {
//        Long teamId = null;
        log.info("createdScheduleDto = {}", scheduleDto);
//        scheduleService.addSchedule(scheduleDto, scheduleDto.getTeamId());

        return "redirect:/schedule";
    }

    @PostMapping("/update")
    public String updateSchedule(@ModelAttribute ScheduleDto scheduleDto, Model model) {
        log.info("updatedScheduleDto = {}", scheduleDto);
        scheduleService.updateSchedule(scheduleDto);
        return "redirect:/schedule";
    }

}
package com.example.waggle.controller;

import com.example.waggle.dto.member.TeamDto;
import com.example.waggle.dto.member.TeamSimpleDto;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.team.ScheduleService;
import com.example.waggle.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final TeamService teamService;
    private final MemberService memberService;

    @GetMapping
    public String teamScheduleInit() {
        //jwt에서 member의 정보를 가져와야한다.
        String username = "";
        List<TeamSimpleDto> simpleTeam = teamService.findSimpleTeam(username);
        TeamSimpleDto teamSimpleDto = simpleTeam.stream().findFirst().get();
        return "redirect:/schedule/"+teamSimpleDto.getName()+"/"+teamSimpleDto.getId();
    }

    @GetMapping("/{teamName}/{teamId}")
    public String teamScheduleForm(@PathVariable Long teamId,
                                   Model model) {
        Optional<TeamDto> byTeamId = teamService.findByTeamId(teamId);
        if (byTeamId.isEmpty()) {
            //error
        }
        model.addAttribute("team", byTeamId.get());
        return "private/scheduleView";
    }
}

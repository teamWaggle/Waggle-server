package com.example.waggle.controller;

import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/")
    public String teamMain(Model model) {
        SecurityUtil.getCurrentUsername();
//        model.addAttribute("teams", )
        return "";
    }

}

package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.dto.board.StorySimpleDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.service.board.StoryService;
import com.example.waggle.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    public final MemberService memberService;
    public final StoryService storyService;

    /**
     * view
     */

    @GetMapping("/{username}")
    public String memberStory(@PathVariable String username,
                              Model model) {
        List<StorySimpleDto> allStoryByMember = storyService.findAllStoryByMember(username);
        model.addAttribute("simpleStories", allStoryByMember);
        return "story/memberStory";
    }

    @GetMapping("/{username}/{boardId}")
    public String story(@PathVariable Long boardId, Model model) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(boardId);
        model.addAttribute("story", storyByBoardId);
        return "story/story";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String writeStoryForm(Model model, @AuthenticationPrincipal MemberSimpleDto memberSimpleDto) {
        // TODO authorization 문제 해결되면 수정
        memberSimpleDto = new MemberSimpleDto("suddiyo", "https://github.com/suddiyo/suddiyo/assets/88311377/4a78ad58-d17a-4e56-9abd-c0848099f9be");

//        String currentUsername = SecurityUtil.getCurrentUsername();
//        log.info("currentUsername = {}", currentUsername);
//        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(currentUsername);
//        log.info("memberSimpleDto = {}", memberSimpleDto);

        StoryDto storyDto = new StoryDto(memberSimpleDto.getUsername(), memberSimpleDto.getProfileImg());
        model.addAttribute("storyDto", storyDto);

        return "story/writeStory";
    }

    @PostMapping("/write")
    public String writeStory(@ModelAttribute StoryDto storyDto) {
        StoryDto savedStoryDto = storyService.saveStory(storyDto);
        return "redirect:/story/" + savedStoryDto.getUsername() + "/" + savedStoryDto.getId();
    }

    /**
     * edit
     */
    @GetMapping("/edit/{boardId}")
    public String editStoryForm(Model model, @PathVariable Long boardId) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(boardId);
        model.addAttribute("story", storyByBoardId);
        return "story/editStory";
    }

    @PostMapping("/edit/{boardId}")
    public String editStory(@ModelAttribute StoryDto storyDto) {
        storyService.changeStory(storyDto);
        String username = storyDto.getUsername();
        Long boardId = storyDto.getId();
        return "redirect:/story/" + username + "/" + boardId;
    }

    /**
     * remove
     */
}

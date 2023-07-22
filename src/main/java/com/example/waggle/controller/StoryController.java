package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.dto.board.StorySimpleDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.service.board.StoryService;
import com.example.waggle.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
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
    public String writeStoryForm(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(username);
        StoryDto storyDto = new StoryDto(memberSimpleDto.getUsername());

        model.addAttribute("storyDto", storyDto);
        model.addAttribute("profileImg", memberSimpleDto.getProfileImg().getStoreFileName());
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
        StoryDto storyDto = storyService.findStoryByBoardId(boardId);
        if (storyDto.getUsername() != SecurityUtil.getCurrentUsername()) {
            // 작성자 외의 접근 error 처리
        }

        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(storyDto.getUsername());

        model.addAttribute("storyDto", storyDto);
        model.addAttribute("profileImg", memberSimpleDto.getProfileImg().getStoreFileName());
        return "story/editStory";
    }

    @PostMapping("/edit/{boardId}")
    public String editStory(@ModelAttribute StoryDto storyDto, @PathVariable Long boardId) {
        StoryDto changedStoryDto = storyService.changeStory(storyDto, boardId);
        String username = changedStoryDto.getUsername();
        log.info("username = {}, boardId = {}", username, boardId);
        return "redirect:/story/" + username + "/" + boardId;
    }




    /**
     * remove
     */
}

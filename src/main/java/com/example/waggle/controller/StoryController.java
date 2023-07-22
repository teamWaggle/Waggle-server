package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.dto.board.StorySimpleDto;
import com.example.waggle.service.board.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    private final StoryService storyService;

    /**
     * view
     */
    @GetMapping
    public String storyMain(Model model) {
        List<StorySimpleDto> allStory = storyService.findAllStory(SecurityUtil.getCurrentUsername());
        model.addAttribute("simpleStories", allStory);
        return "main";
    }

    @GetMapping("/{username}")
    public String storyPrivateMain(@PathVariable String username,
                                   Model model) {
        List<StorySimpleDto> allStoryByMember = storyService.findAllStoryByMember(username);
        model.addAttribute("simpleStories", allStoryByMember);
        return "story/memberStory";
    }

    @GetMapping("/{username}/{boardId}")
    public String storySingleForm(@PathVariable String username, @PathVariable Long boardId, Model model) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(username, boardId);
        model.addAttribute("story", storyByBoardId);
        return "story/story";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String storySingleWriteForm(Model model) {
        model.addAttribute("story", new StoryDto());
        return "story/addStory";
    }

    @PostMapping("/write")
    public String storySingleWrite(@ModelAttribute StoryDto storyDto) {
        storyService.saveStory(SecurityUtil.getCurrentUsername(),storyDto);
        String username = storyDto.getUsername();
        Long boardId = storyDto.getId();
        return "redirect:/story/" + username + "/" + boardId;
    }

    /**
     * edit
     */
    @GetMapping("/edit/{boardId}")
    public String storySingleEditForm(Model model, @PathVariable Long boardId) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(SecurityUtil.getCurrentUsername(), boardId);
        model.addAttribute("story", storyByBoardId);
        return "story/editStory";
    }

    @PostMapping("/edit/{boardId}")
    public String storySingleEdit(@ModelAttribute StoryDto storyDto) {
        storyService.changeStory(SecurityUtil.getCurrentUsername(),storyDto);
        String username = storyDto.getUsername();
        Long boardId = storyDto.getId();
        return "redirect:/story/" + username + "/" + boardId;
    }

    /**
     * remove
     */
}

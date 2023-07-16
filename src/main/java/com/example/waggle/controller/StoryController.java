package com.example.waggle.controller;

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

    public final StoryService storyService;

    /**
     * view
     */
    @GetMapping
    public String storyMain(Model model) {
        List<StorySimpleDto> allStory = storyService.findAllStory();
        model.addAttribute("simpleStories", allStory);
        return "public/main";
    }

    @GetMapping("/{username}")
    public String storyPrivateMain(@PathVariable String username,
                                   Model model) {
        List<StorySimpleDto> allStoryByMember = storyService.findAllStoryByMember(username);
        model.addAttribute("simpleStories", allStoryByMember);
        return "private/storyView";
    }

    @GetMapping("/{username}/{boardId}")
    public String storySingleForm(@PathVariable Long boardId, Model model) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(boardId);
        model.addAttribute("story", storyByBoardId);
        return "public/story/storySingle";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String storySingleWriteForm(Model model) {
        model.addAttribute("story", new StoryDto());
        return "/private/story/storyWrite";
    }

    @PostMapping("/write")
    public String storySingleWrite(@ModelAttribute StoryDto storyDto) {
        storyService.saveStory(storyDto);
        String username = storyDto.getUsername();
        Long boardId = storyDto.getId();
        return "redirect:/story/" + username + "/" + boardId;
    }

    /**
     * edit
     */
    @GetMapping("/edit/{boardId}")
    public String storySingleEditForm(Model model, @PathVariable Long boardId) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(boardId);
        model.addAttribute("story", storyByBoardId);
        return "private/story/storyEdit";
    }

    @PostMapping("/edit/{boardId}")
    public String storySingleEdit(@ModelAttribute StoryDto storyDto) {
        storyService.changeStory(storyDto);
        String username = storyDto.getUsername();
        Long boardId = storyDto.getId();
        return "redirect:/story/" + username + "/" + boardId;
    }

    /**
     * remove
     */
}

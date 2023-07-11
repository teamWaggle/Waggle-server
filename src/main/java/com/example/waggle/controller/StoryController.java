package com.example.waggle.controller;

import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.dto.board.StorySimpleDto;
import com.example.waggle.service.board.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    public final StoryService storyService;

    /**
     *view
     */
    @GetMapping
    public String storyMain(){
        return "public/main";
    }

    @GetMapping("/{username}/{boardId}")
    public String storySingleForm(@PathVariable Long boardId, @PathVariable String username,
                              Model model) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(boardId);
        model.addAttribute("story",storyByBoardId);
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
        return "redirect:/story/"+username+"/"+boardId;
    }
    /**
     * edit
     */

    /**
     * remove
     */
}

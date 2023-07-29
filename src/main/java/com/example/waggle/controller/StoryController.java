package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.service.board.StoryService;
import com.example.waggle.service.member.MemberService;
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
    private final MemberService memberService;

    /**
     * view
     */
    @GetMapping
    public String storyMain(Model model) {
        List<StorySimpleViewDto> allStory = storyService.findAllStory(SecurityUtil.getCurrentUsername());
        model.addAttribute("simpleStories", allStory);
        return "index";
    }


    @GetMapping("/{username}")
    public String memberStory(@PathVariable String username,
                              Model model) {
        List<StorySimpleViewDto> allStoryByMember = storyService.findAllStoryByMember(username);
        model.addAttribute("simpleStories", allStoryByMember);
        return "story/memberStory";
    }

    @GetMapping("/{username}/{boardId}")
    public String singleStoryForm(@PathVariable String username,
                                  @PathVariable Long boardId,
                                  Model model) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(username, boardId);
        model.addAttribute("storyDto", storyByBoardId);
        return "story/story";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String singleStoryWriteForm(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(username);
        StoryViewDto storyDto = new StoryViewDto(memberSimpleDto.getUsername());

        model.addAttribute("storyDto", storyDto);
        model.addAttribute("profileImg", memberSimpleDto.getProfileImg());

        return "story/writeStory";
    }

    @PostMapping("/write")
    public String singleStoryWrite(@ModelAttribute StoryViewDto storyDto) {
        Long boardId = storyService.saveStory(SecurityUtil.getCurrentUsername(), storyDto);
        String username = storyDto.getUsername();
        return "redirect:/story/" + username + "/" + boardId;

    }

    /**
     * edit
     */
    @GetMapping("/edit/{boardId}")
    public String singleStoryEditForm(Model model, @PathVariable Long boardId) {
//        if (storyDto.getUsername() != SecurityUtil.getCurrentUsername()) {
//            // 작성자 외의 접근 error 처리
//        }
        StoryViewDto storyDto = storyService.findStoryByBoardId(SecurityUtil.getCurrentUsername(), boardId);
        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(storyDto.getUsername());
        model.addAttribute("storyDto", storyDto);
        model.addAttribute("profileImg", memberSimpleDto.getProfileImg().getStoreFileName());

        return "story/editStory";
    }

    @PostMapping("/edit/{boardId}")
    public String singleStoryEdit(@ModelAttribute StoryViewDto storyDto,
                                  @PathVariable Long boardId) {
        String username = storyService.changeStory(storyDto, boardId);
        return "redirect:/story/" + username + "/" + boardId;
    }


    /**
     * remove
     */
}

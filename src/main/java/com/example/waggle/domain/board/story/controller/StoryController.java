package com.example.waggle.domain.board.story.controller;

import com.example.waggle.domain.board.story.dto.StoryDetailDto;
import com.example.waggle.domain.board.story.dto.StorySummaryDto;
import com.example.waggle.domain.board.story.dto.StoryWriteDto;
import com.example.waggle.domain.board.story.service.StoryService;
import com.example.waggle.commons.security.SecurityUtil;
import com.example.waggle.domain.member.dto.MemberSummaryDto;
import com.example.waggle.domain.member.service.MemberQueryService;

import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/story")
public class StoryController {


    private final StoryService storyService;
    private final MemberQueryService memberQueryService;

    /**
     * view
     */
    @GetMapping
    public String storyMain(Model model) {
        /*
        List<StorySimpleViewDto> storySimpleViewDtos = storyService.findAll();
        model.addAttribute("storySimpleViewDtos", storySimpleViewDtos);
        model.addAttribute("simpleStories", allStory);
        */
        return "story/main";
    }


    @GetMapping("/{username}")
    public String memberStory(@PathVariable String username,
                              Model model) {
        Pageable pageable = PageRequest.of(0, 3);
        Page<StorySummaryDto> storiesByUsername = storyService.getPagedStoriesByUsername(username, pageable);
        model.addAttribute("simpleStories", storiesByUsername);
        return "story/memberStory";
    }

    @GetMapping("/{username}/{boardId}")
    public String singleStoryForm(@PathVariable String username,
                                  @PathVariable Long boardId,
                                  Model model) {
        StoryDetailDto storyByBoardId = storyService.getStoryByBoardId(boardId);
        model.addAttribute("storyDto", storyByBoardId);
        return "story/story";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String singleStoryWriteForm(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        MemberSummaryDto memberSummaryDto = memberQueryService.getMemberSummaryDto(username);
        StoryWriteDto storyDto = new StoryWriteDto();

        model.addAttribute("storyDto", storyDto);
        model.addAttribute("memberSimpleDto", memberSummaryDto);

        return "story/writeStory";
    }

    @PostMapping("/write")
    public String singleStoryWrite(@Validated @ModelAttribute StoryWriteDto storyDto,
                                   BindingResult bindingResult) throws IOException {
        //validation
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "story/writeStory";
        }

        String username = SecurityUtil.getCurrentUsername();
        Long boardId = storyService.createStory(storyDto, new ArrayList<>(), null);
        return "redirect:/story/" + username + "/" + boardId;
    }

    @GetMapping("/edit/{boardId}")
    public String singleStoryEditForm(Model model, @PathVariable Long boardId) {
//        if (storyDto.getUsername() != SecurityUtil.getCurrentUsername()) {
//            // 작성자 외의 접근 error 처리
//        }
        if (!storyService.validateMember(boardId)) {
            //error
            return "redirect:/story";
        }
        StoryWriteDto storyDto = storyService.getStoryWriteDtoByBoardId(boardId);
        MemberSummaryDto memberSummaryDto = memberQueryService.getMemberSummaryDto(SecurityUtil.getCurrentUsername());
        model.addAttribute("storyDto", storyDto);
        model.addAttribute("profileImg", memberSummaryDto.getProfileImg());

        return "story/editStory";
    }

    @PostMapping("/edit/{boardId}")
    public String singleStoryEdit(@ModelAttribute StoryWriteDto storyDto,
                                  @PathVariable Long boardId) {
//        String username = storyService.updateStory(storyDto);
//        return "redirect:/story/" + username + "/" + boardId;
        return null;
    }


    /**
     * remove
     */
}

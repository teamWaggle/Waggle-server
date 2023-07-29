package com.example.waggle.controller;

import com.example.waggle.component.file.FileStore;

import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.service.board.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final StoryService storyService;
    private final FileStore fileStore;

    @GetMapping("/")
    public String home(Model model) {
//        storyService.findAllStory();

        List<StorySimpleViewDto> storySimpleViewDtos = new ArrayList<>();
        StorySimpleViewDto storySimpleViewDto1 = StorySimpleViewDto.builder()
                .username("story1")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleViewDto storySimpleViewDto2 = StorySimpleViewDto.builder()
                .username("story2")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleViewDto storySimpleViewDto3 = StorySimpleViewDto.builder()
                .username("story3")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleViewDto storySimpleViewDto4 = StorySimpleViewDto.builder()
                .username("story4")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleViewDto storySimpleViewDto5 = StorySimpleViewDto.builder()
                .username("story5")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();


        storySimpleViewDtos.add(storySimpleViewDto1);
        storySimpleViewDtos.add(storySimpleViewDto2);
        storySimpleViewDtos.add(storySimpleViewDto3);
        storySimpleViewDtos.add(storySimpleViewDto4);
        storySimpleViewDtos.add(storySimpleViewDto5);

        model.addAttribute("storySimpleViewDtos", storySimpleViewDtos);
        return "index";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}

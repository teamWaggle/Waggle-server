package com.example.waggle.controller;

import com.example.waggle.component.file.FileStore;
import com.example.waggle.dto.board.StorySimpleDto;
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

        List<StorySimpleDto> storySimpleDtoList = new ArrayList<>();
        StorySimpleDto storySimpleDto1 = StorySimpleDto.builder()
                .username("story1")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleDto storySimpleDto2 = StorySimpleDto.builder()
                .username("story2")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleDto storySimpleDto3 = StorySimpleDto.builder()
                .username("story3")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleDto storySimpleDto4 = StorySimpleDto.builder()
                .username("story4")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();

        StorySimpleDto storySimpleDto5 = StorySimpleDto.builder()
                .username("story5")
                .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/9323405a-58ba-4c41-adb6-3b2c93f5558a").build();


        storySimpleDtoList.add(storySimpleDto1);
        storySimpleDtoList.add(storySimpleDto2);
        storySimpleDtoList.add(storySimpleDto3);
        storySimpleDtoList.add(storySimpleDto4);
        storySimpleDtoList.add(storySimpleDto5);

        model.addAttribute("storySimpleDtoList", storySimpleDtoList);
        return "index";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}

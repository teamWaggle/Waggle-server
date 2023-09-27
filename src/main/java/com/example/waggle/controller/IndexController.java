package com.example.waggle.controller;

import com.example.waggle.component.TestDataInit;
import com.example.waggle.component.file.FileStore;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.service.board.StoryService;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.utils.DateUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController implements ErrorController {
    private final FileStore fileStore;

    @GetMapping({"/", "/error"})
    public String index() {
        return "index.html";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}

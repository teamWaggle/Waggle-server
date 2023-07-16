package com.example.waggle.controller;

import com.example.waggle.dto.board.QuestionDto;
import com.example.waggle.dto.board.QuestionSimpleDto;
import com.example.waggle.service.board.QuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    public final QuestionService questionService;

    /**
     * view
     */
    @GetMapping
    public String questionMain(Model model) {
        List<QuestionSimpleDto> allQuestion = questionService.findAllQuestion();
        model.addAttribute("simpleQuestions", allQuestion);
        return "public/question/questionMain";
    }

    @GetMapping("/{username}/{title}/{boardId}")
    public String questionSingleForm(@PathVariable Long boardId,
                                     Model model) {
        QuestionDto questionByBoardId = questionService.findQuestionByBoardId(boardId);
        model.addAttribute("question", questionByBoardId);
        return "public/question/questionSingle";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String questionSingleWriteForm(Model model) {
        model.addAttribute("question", new QuestionDto());
        return "/private/question/questionWrite";
    }

    @PostMapping("/write")
    public String questionSingleWrite(@ModelAttribute QuestionDto questionDto) {
        questionService.saveQuestion(questionDto);
        String username = questionDto.getUsername();
        Long boardId = questionDto.getId();
        return "redirect:/question/" + username + "/" + boardId;
    }

    /**
     * edit
     */
    @GetMapping("/edit/{title}/{boardId}")
    public String questionSingleEditForm(Model model, @PathVariable Long boardId) {
        QuestionDto questionDto = questionService.findQuestionByBoardId(boardId);
        model.addAttribute("question", questionDto);
        return "private/question/questionEdit";
    }

    @PostMapping("/edit/{title}/{boardId}")
    public String questionSingleEdit(@ModelAttribute QuestionDto questionDto) {
        questionService.changeQuestion(questionDto);
        String username = questionDto.getUsername();
        String title = questionDto.getTitle();
        Long boardId = questionDto.getId();
        return "redirect:/question/" + username + "/" + title + "/" + boardId;
    }

    /**
     * remove
     */
}

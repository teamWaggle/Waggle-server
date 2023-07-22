package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.QuestionDto;
import com.example.waggle.dto.board.QuestionSimpleDto;
import com.example.waggle.dto.board.StorySimpleDto;
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
        List<QuestionSimpleDto> allQuestion = questionService.findAllQuestion(SecurityUtil.getCurrentUsername());
        model.addAttribute("simpleQuestions", allQuestion);
        return "question/questionList";
    }

    @GetMapping("/{username}")
    public String questionPrivateMain(Model model) {
        List<QuestionSimpleDto> allQuestionByMember = questionService
                .findAllQuestionByMember(SecurityUtil.getCurrentUsername());
        model.addAttribute("simpleQuestions", allQuestionByMember);
        return "question/memberQuestion";
    }

    @GetMapping("/{username}/{title}/{boardId}")
    public String questionSingleForm(@PathVariable String username,
                                     @PathVariable Long boardId,
                                     Model model) {
        QuestionDto questionByBoardId = questionService.findQuestionByBoardId(username, boardId);
        model.addAttribute("question", questionByBoardId);
        return "question/question";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String questionSingleWriteForm(Model model) {
        model.addAttribute("question", new QuestionDto());
        return "question/addQuestion";
    }

    @PostMapping("/write")
    public String questionSingleWrite(@ModelAttribute QuestionDto questionDto) {
        Long questionId = questionService.saveQuestion(SecurityUtil.getCurrentUsername(), questionDto);
        String username = questionDto.getUsername();
        String title = questionDto.getTitle();
        return "redirect:/question/" + username + "/" +title + "/"+ questionId;
    }

    /**
     * edit
     */
    @GetMapping("/edit/{title}/{boardId}")
    public String questionSingleEditForm(Model model, @PathVariable Long boardId) {
        QuestionDto questionDto = questionService.findQuestionByBoardId(SecurityUtil.getCurrentUsername(), boardId);
        model.addAttribute("question", questionDto);
        return "question/editQuestion";
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

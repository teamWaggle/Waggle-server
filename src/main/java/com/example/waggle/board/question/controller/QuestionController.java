package com.example.waggle.board.question.controller;

import com.example.waggle.commons.security.SecurityUtil;
import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.dto.QuestionDetailDto;
import com.example.waggle.board.question.dto.QuestionWriteDto;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.board.question.service.QuestionService;
import com.example.waggle.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    /**
     * view
     */
    @GetMapping
    public String questionMain(Model model) {
        List<QuestionSummaryDto> allQuestion = questionService.getQuestions();
        model.addAttribute("simpleQuestions", allQuestion);
        return "question/questionList";
    }

    @GetMapping("/{username}")
    public String memberQuestion(Model model, @PathVariable String username) {
        List<QuestionSummaryDto> allQuestionByMember = questionService
                .getQuestionsByUsername(username);
        model.addAttribute("simpleQuestions", allQuestionByMember);
        return "question/memberQuestion";
    }

    @GetMapping("/{username}/{title}/{boardId}")
    public String singleQuestionForm(@PathVariable String username,
                                     @PathVariable Long boardId,
                                     Model model) {
        QuestionDetailDto questionByBoardId = questionService.getQuestionByBoardId(boardId);
        model.addAttribute("question", questionByBoardId);
        return "question/question";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String singleQuestionWriteForm(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        MemberSummaryDto memberSummaryDto = memberService.getMemberSummaryDto(username);
        QuestionDetailDto questionDto = new QuestionDetailDto(username);

        model.addAttribute("profileImg", memberSummaryDto.getProfileImg());
        model.addAttribute("question", questionDto);
        return "question/writeQuestion";
    }

    @PostMapping("/write")
    public String singleQuestionWrite(@Validated @ModelAttribute QuestionWriteDto questionDto,
                                      BindingResult bindingResult) {
        //validation
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "question/writeQuetion";
        }
        String username = SecurityUtil.getCurrentUsername();
        Long questionId = questionService.createQuestion(questionDto);
        String title = questionDto.getTitle();
        return "redirect:/question/" + username + "/" +title + "/"+ questionId;
    }

    /**
     * edit
     */
    @GetMapping("/edit/{title}/{boardId}")
    public String questionSingleEditForm(Model model, @PathVariable Long boardId) {
        QuestionDetailDto questionDto = questionService.getQuestionByBoardId(boardId);
        MemberSummaryDto memberSummaryDto = memberService.getMemberSummaryDto(questionDto.getUsername());

        model.addAttribute("profileImg", memberSummaryDto.getProfileImg().getStoreFileName());
        model.addAttribute("question", questionDto);
        return "question/editQuestion";
    }

    @PostMapping("/edit/{title}/{boardId}")
    public String singleQuestionEdit(@ModelAttribute QuestionWriteDto questionDto,
                                     @PathVariable Long boardId) {
//        String username = questionService.updateQuestion(questionDto, boardId);
//        String title = questionDto.getTitle();
//        return "redirect:/question/" + username + "/" + title + "/" + boardId;
        return null;
    }

    /**
     * remove
     */
}

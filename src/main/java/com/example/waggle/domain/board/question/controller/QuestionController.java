package com.example.waggle.domain.board.question.controller;

import com.example.waggle.domain.board.question.dto.QuestionDetailDto;
import com.example.waggle.domain.board.question.dto.QuestionSummaryDto;
import com.example.waggle.domain.board.question.dto.QuestionWriteDto;
import com.example.waggle.domain.board.question.service.QuestionService;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.domain.member.dto.MemberSummaryDto;
import com.example.waggle.domain.member.service.MemberQueryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberQueryService memberQueryService;

    private Sort latestSorting = Sort.by("createdDate").descending();


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
        Pageable pageable = PageRequest.of(0, 10, latestSorting);
        Page<QuestionSummaryDto> questionsByUsername = questionService
                .getPagedQuestionsByUsername(username, pageable);
        model.addAttribute("simpleQuestions", questionsByUsername);
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
        MemberSummaryDto memberSummaryDto = memberQueryService.getMemberSummaryDto(username);
        QuestionDetailDto questionDto = new QuestionDetailDto(username);

        model.addAttribute("profileImg", memberSummaryDto.getProfileImg());
        model.addAttribute("question", questionDto);
        return "question/writeQuestion";
    }

    @PostMapping("/write")
    public String singleQuestionWrite(@Validated @ModelAttribute QuestionWriteDto questionDto,
                                      BindingResult bindingResult) throws IOException {
        //validation
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "question/writeQuetion";
        }
        String username = SecurityUtil.getCurrentUsername();
        Long questionId = questionService.createQuestion(questionDto, new ArrayList<>());
        String title = questionDto.getTitle();
        return "redirect:/question/" + username + "/" +title + "/"+ questionId;
    }

    /**
     * edit
     */
    @GetMapping("/edit/{title}/{boardId}")
    public String questionSingleEditForm(Model model, @PathVariable Long boardId) {
        QuestionDetailDto questionDto = questionService.getQuestionByBoardId(boardId);
        MemberSummaryDto memberSummaryDto = memberQueryService.getMemberSummaryDto(questionDto.getUsername());

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

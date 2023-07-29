package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.dto.board.question.QuestionViewDto;
import com.example.waggle.dto.board.question.QuestionSimpleViewDto;
import com.example.waggle.dto.board.question.QuestionWriteDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.service.board.QuestionService;

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
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    /**
     * view
     */
    @GetMapping
    public String questionMain(Model model) {
        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion(SecurityUtil.getCurrentUsername());
        model.addAttribute("simpleQuestions", allQuestion);
        return "question/questionList";
    }

    @GetMapping("/{username}")
    public String memberQuestion(Model model) {
        List<QuestionSimpleViewDto> allQuestionByMember = questionService
                .findAllQuestionByMember(SecurityUtil.getCurrentUsername());
        model.addAttribute("simpleQuestions", allQuestionByMember);
        return "question/memberQuestion";
    }

    @GetMapping("/{username}/{title}/{boardId}")
    public String singleQuestionForm(@PathVariable String username,
                                     @PathVariable Long boardId,
                                     Model model) {
        QuestionViewDto questionByBoardId = questionService.findQuestionByBoardId(username, boardId);
        model.addAttribute("question", questionByBoardId);
        return "question/question";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String singleQuestionWriteForm(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(username);
        QuestionViewDto questionDto = new QuestionViewDto(username);

        model.addAttribute("profileImg", memberSimpleDto.getProfileImg().getStoreFileName());
        model.addAttribute("question", questionDto);
        return "question/writeQuestion";
    }

    @PostMapping("/write")
    public String singleQuestionWrite(@ModelAttribute QuestionWriteDto questionDto) {

        String username = SecurityUtil.getCurrentUsername();
        Long questionId = questionService.saveQuestion(SecurityUtil.getCurrentUsername(), questionDto);
        String title = questionDto.getTitle();
        return "redirect:/question/" + username + "/" +title + "/"+ questionId;
    }

    /**
     * edit
     */
    @GetMapping("/edit/{title}/{boardId}")
    public String questionSingleEditForm(Model model, @PathVariable Long boardId) {
        QuestionViewDto questionDto = questionService.findQuestionByBoardId(SecurityUtil.getCurrentUsername(), boardId);
        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(questionDto.getUsername());

        model.addAttribute("profileImg", memberSimpleDto.getProfileImg().getStoreFileName());
        model.addAttribute("question", questionDto);
        return "question/editQuestion";
    }

    @PostMapping("/edit/{title}/{boardId}")
    public String singleQuestionEdit(@ModelAttribute QuestionWriteDto questionDto,
                                     @PathVariable Long boardId) {
        String username = questionService.changeQuestion(questionDto, boardId);
        String title = questionDto.getTitle();
        return "redirect:/question/" + username + "/" + title + "/" + boardId;
    }

    /**
     * remove
     */
}

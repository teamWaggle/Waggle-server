package com.example.waggle.board.question.controller;

import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.service.QuestionService;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.dto.page.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
@Tag(name = "Question API", description = "질문 API")
public class QuestionApiController {
    private final QuestionService questionService;
    private final FileStore fileStore;

    @Operation(
            summary = "전체 질문 목록 조회",
            description = "전체 질문 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "질문 조회 성공. 전체 질문 목록을 반환합니다."
    )
    @GetMapping("/all")
    public ResponseEntity<?> question(@RequestParam(defaultValue = "1")int currentPage) {  // TODO 인기순, 최신순에 따른 필터링 필요
        List<QuestionSummaryDto> questions = questionService.getQuestions();
        log.info("allStoryByMember = {}", questions);
        Pagination pagination = new Pagination(currentPage, questions);

        return ResponseEntity.ok(pagination);
    }
}

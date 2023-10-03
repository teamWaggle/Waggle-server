package com.example.waggle.board.question.controller;

import com.example.waggle.board.question.dto.QuestionDetailDto;
import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.dto.QuestionWriteDto;
import com.example.waggle.board.question.service.QuestionService;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.dto.page.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            summary = "질문 작성",
            description = "사용자가 질문을 작성합니다. 작성한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "질문 작성 성공. 작성한 스토리의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 질문 작성에 실패했습니다."
    )
    @PostMapping("/write")
    public ResponseEntity<?> singleQuestionWrite(@RequestPart QuestionWriteDto questionDto) throws IOException {
        Long questionId = questionService.createQuestion(questionDto);
        return ResponseEntity.ok(questionId);
    }

    @Operation(
            summary = "스토리 수정",
            description = "사용자가 스토리를 수정합니다. 수정한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 수정 성공. 수정한 스토리의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 수정에 실패했습니다."
    )
    @PostMapping("/edit/{boardId}")
    public ResponseEntity<?> singleQuestionEdit(@ModelAttribute QuestionWriteDto questionDto, @PathVariable Long boardId) {
        questionService.updateQuestion(boardId, questionDto);
        return ResponseEntity.ok(boardId);
    }

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
        List<QuestionSummaryDto> allQuestions = questionService.getQuestions();
        log.info("allStoryByMember = {}", allQuestions);
        Pagination pagination = new Pagination(currentPage, allQuestions);

        return ResponseEntity.ok(pagination);
    }
    @Operation(
            summary = "사용자의 질문 목록 조회",
            description = "특정 사용자가 작성한 질문 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "질문 조회 성공. 사용자가 작성한 질문 목록을 반환합니다."
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다."
    )
    @GetMapping("/{username}")
    public ResponseEntity<?> memberQuestion(@PathVariable String username) {
        List<QuestionSummaryDto> allQuestionsByMember = questionService.getQuestionsByUsername(username);
        return ResponseEntity.ok(allQuestionsByMember);
    }
    @Operation(
            summary = "특정 질문 조회",
            description = "특정 질문의 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "질문 조회 성공. 특정 질문의 상세 정보를 반환합니다."
    )
    @ApiResponse(
            responseCode = "404",
            description = "질문을 찾을 수 없음. 지정된 질문 ID에 해당하는 질문을 찾을 수 없습니다."
    )
    @GetMapping("/{username}/{boardId}")
    public ResponseEntity<?> singleQuestionForm(@PathVariable String username, @PathVariable Long boardId) {
        QuestionDetailDto questionByBoardId = questionService.getQuestionByBoardId(boardId);
        return ResponseEntity.ok(questionByBoardId);
    }
}

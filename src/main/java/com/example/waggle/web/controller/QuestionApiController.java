package com.example.waggle.web.controller;

import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.question.service.QuestionQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.dto.question.QuestionDetailDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import com.example.waggle.web.dto.question.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionWriteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/questions")
@RestController
@Tag(name = "Question API", description = "질문 API")
public class QuestionApiController {

    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private final Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "질문 작성", description = "사용자가 질문을 작성합니다. 작성한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "질문 작성 성공. 작성한 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 질문 작성에 실패했습니다.")
    @PostMapping
    public ApiResponseDto<Long> createQuestion(@RequestPart QuestionRequest.QuestionWriteDto request,
                                                           @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        Long boardId = questionCommandService.createQuestion(request, multipartFiles);
    return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "스토리 수정", description = "사용자가 스토리를 수정합니다. 수정한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 수정 성공. 수정한 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 수정에 실패했습니다.")
    @PutMapping("/{boardId}")
    public ResponseEntity<Long> updateQuestion(@PathVariable Long boardId,
                                               @ModelAttribute QuestionWriteDto questionDto,
                                               @RequestPart List<MultipartFile> multipartFiles) throws IOException {
        questionCommandService.updateQuestion(boardId, questionDto, multipartFiles);
        return ResponseEntity.ok(boardId);
    }

    @Operation(summary = "전체 질문 목록 조회", description = "전체 질문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "질문 조회 성공. 전체 질문 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<Page<QuestionSummaryDto>> getAllQuestions(@RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<QuestionSummaryDto> questions = questionQueryService.getPagedQuestions(pageable);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "사용자의 질문 목록 조회", description = "특정 사용자가 작성한 질문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "질문 조회 성공. 사용자가 작성한 질문 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{username}")
    public ResponseEntity<Page<QuestionSummaryDto>> getQuestionsByUsername(@RequestParam(defaultValue = "0") int currentPage,
                                                                           @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<QuestionSummaryDto> questionsByUsername = questionQueryService.getPagedQuestionsByUsername(username, pageable);
        return ResponseEntity.ok(questionsByUsername);
    }

    @Operation(summary = "특정 질문 조회", description = "특정 질문의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "질문 조회 성공. 특정 질문의 상세 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음. 지정된 질문 ID에 해당하는 질문을 찾을 수 없습니다.")
    @GetMapping("/{boardId}")
    public ResponseEntity<QuestionDetailDto> getQuestionByBoardId(@PathVariable Long boardId) {
        QuestionDetailDto questionByBoardId = questionQueryService.getQuestionByBoardId(boardId);
        return ResponseEntity.ok(questionByBoardId);
    }
}

package com.example.waggle.web.controller;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.question.service.QuestionQueryService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.QuestionConverter;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import com.example.waggle.web.dto.question.QuestionResponse;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Question API", description = "질문 API")
public class QuestionApiController {

    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private final RecommendQueryService recommendQueryService;
    private final Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "질문 작성 🔑", description = "사용자가 질문을 작성합니다. 작성한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createQuestion(
            @RequestPart("createQuestionRequest") @Validated QuestionRequest createQuestionRequest,
            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        Long boardId = questionCommandService.createQuestion(createQuestionRequest, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "질문 수정 🔑", description = "사용자가 질문 수정합니다. 수정한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{questionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateQuestion(@PathVariable("questionId") Long questionId,
                                               @RequestPart("updateQuestionRequest") @Validated QuestionRequest updateQuestionRequest,
                                               @RequestPart("updateMediaRequest") MediaUpdateDto updateMediaRequest,
                                               @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        updateMediaRequest.getMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        updateMediaRequest.getDeleteMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        questionCommandService.updateQuestionV2(questionId, updateQuestionRequest, updateMediaRequest, multipartFiles);
        return ApiResponseDto.onSuccess(questionId);
    }

    @Operation(summary = "전체 질문 목록 조회", description = "전체 질문 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping
    public ApiResponseDto<QuestionSummaryListDto> getAllQuestions(
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Question> questions = questionQueryService.getPagedQuestions(pageable);
        QuestionSummaryListDto listDto = QuestionConverter.toListDto(questions);
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 질문 목록 조회", description = "특정 사용자가 작성한 질문 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/member/{memberId}")
    public ApiResponseDto<QuestionSummaryListDto> getQuestionsByUsername(@PathVariable("memberId") Long memberId,
                                                                         @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Question> questions = questionQueryService.getPagedQuestionByMemberId(memberId, pageable);
        QuestionSummaryListDto listDto = QuestionConverter.toListDto(questions);
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 질문 조회", description = "특정 질문의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{questionId}")
    public ApiResponseDto<QuestionResponse.QuestionDetailDto> getQuestionByBoardId(
            @PathVariable("questionId") Long questionId) {
        Question questionByBoardId = questionQueryService.getQuestionByBoardId(questionId);
        QuestionResponse.QuestionDetailDto detailDto = QuestionConverter.toDetailDto(questionByBoardId);
        recommendQueryService.getRecommendValues(detailDto);
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "질문 삭제 🔑", description = "특정 질문을 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등 모두 삭제됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{questionId}")
    public ApiResponseDto<Boolean> deleteQuestion(@PathVariable("questionId") Long questionId) {
        questionCommandService.deleteQuestion(questionId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    // TODO 사용자가 작성한 대답과 관련된 question list 가져오기
}

package com.example.waggle.web.controller;

import static com.example.waggle.web.dto.question.QuestionResponse.QuestionDetailDto;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.question.service.QuestionQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.converter.QuestionConverter;
import com.example.waggle.web.dto.question.QuestionRequest;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryListDto;
import com.example.waggle.web.dto.question.QuestionResponse.RepresentativeQuestionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @AuthUser Member member) {
        List<String> removedPrefixMedia = createQuestionRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        createQuestionRequest.setMediaList(removedPrefixMedia);
        Long boardId = questionCommandService.createQuestion(createQuestionRequest, member);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "질문 수정 🔑", description = "사용자가 질문 수정합니다. 수정한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{questionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateQuestion(@PathVariable("questionId") Long questionId,
                                               @RequestPart("updateQuestionRequest") @Validated QuestionRequest updateQuestionRequest,
                                               @AuthUser Member member) {
        List<String> removedPrefixMedia = updateQuestionRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        updateQuestionRequest.setMediaList(removedPrefixMedia);
        questionCommandService.updateQuestion(questionId, updateQuestionRequest, member);
        return ApiResponseDto.onSuccess(questionId);
    }

    @Operation(summary = "질문 상태 변경 🔑", description = "사용자가 질문 상태 변경합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{questionId}/status")
    public ApiResponseDto<Long> convertStatus(@PathVariable("questionId") Long questionId,
                                              @AuthUser Member member) {
        questionCommandService.convertStatus(questionId, member);
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
        setRecommendInList(listDto.getQuestionList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "대표 질문 조회", description = "대표 사이렌을 조회합니다. 미해결 인기순으로 정렬하고, 상단 3개의 사이렌을 반환합니다.")
    @ApiErrorCodeExample({ErrorStatus._INTERNAL_SERVER_ERROR})
    @GetMapping("/representative")
    public ApiResponseDto<RepresentativeQuestionDto> getRepresentativeQuestionList() {
        List<Question> representativeQuestionList = questionQueryService.getRepresentativeQuestionList();
        RepresentativeQuestionDto listDto = QuestionConverter.toRepresentativeQuestionDto(
                representativeQuestionList);
        setRecommendInList(listDto.getQuestionList());
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
        setRecommendInList(listDto.getQuestionList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 질문 조회", description = "특정 질문의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{questionId}")
    public ApiResponseDto<QuestionDetailDto> getQuestionByBoardId(
            @PathVariable("questionId") Long questionId) {
        questionCommandService.increaseQuestionViewCount(questionId);
        Question questionByBoardId = questionQueryService.getQuestionByBoardId(questionId);
        QuestionDetailDto detailDto = QuestionConverter.toDetailDto(questionByBoardId);
        detailDto.setRecommendationInfo(recommendQueryService.getRecommendationInfo(
                questionId,
                SecurityUtil.getCurrentUsername())
        );
        return ApiResponseDto.onSuccess(detailDto);
    }


    @Operation(summary = "질문 삭제 🔑", description = "특정 질문을 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등 모두 삭제됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{questionId}")
    public ApiResponseDto<Boolean> deleteQuestion(@PathVariable("questionId") Long questionId,
                                                  @AuthUser Member member) {
        questionCommandService.deleteQuestion(questionId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    private void setRecommendInList(List<QuestionSummaryDto> questionList) {
        questionList
                .forEach(question ->
                        question.setRecommendationInfo(
                                recommendQueryService.getRecommendationInfo(
                                        question.getBoardId(),
                                        SecurityUtil.getCurrentUsername()))
                );
    }

    // TODO 사용자가 작성한 대답과 관련된 question list 가져오기
}

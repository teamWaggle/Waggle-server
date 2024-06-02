package com.example.waggle.domain.board.presentation.controller;

import com.example.waggle.domain.board.application.question.QuestionCacheService;
import com.example.waggle.domain.board.application.question.QuestionCommandService;
import com.example.waggle.domain.board.application.question.QuestionQueryService;
import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.converter.QuestionConverter;
import com.example.waggle.domain.board.presentation.dto.question.QuestionRequest;
import com.example.waggle.domain.board.presentation.dto.question.QuestionResponse.QuestionSummaryDto;
import com.example.waggle.domain.board.presentation.dto.question.QuestionResponse.QuestionSummaryListDto;
import com.example.waggle.domain.board.presentation.dto.question.QuestionResponse.RepresentativeQuestionDto;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.application.query.RecommendQueryService;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.ObjectUtil;
import com.example.waggle.global.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.domain.board.presentation.dto.question.QuestionResponse.QuestionDetailDto;
import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.AUTH;
import static com.example.waggle.global.util.PageUtil.QUESTION_SIZE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/questions")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Question API", description = "질문 API")
public class QuestionApiController {

    private final QuestionCacheService questionCacheService;
    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private final RecommendQueryService recommendQueryService;
    //TODO remove latestSorting and use PageUtil variable
    private final Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "질문 작성 🔑", description = "사용자가 질문을 작성합니다. 작성한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEDIA_PREFIX_IS_WRONG
    }, status = AUTH)
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
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEDIA_PREFIX_IS_WRONG,
            ErrorStatus.BOARD_CANNOT_EDIT_OTHERS,
            ErrorStatus.BOARD_NOT_FOUND
    }, status = AUTH)
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
    @ApiErrorCodeExample(value = {
            ErrorStatus.BOARD_CANNOT_EDIT_OTHERS,
            ErrorStatus.BOARD_NOT_FOUND,
            ErrorStatus.BOARD_INVALID_TYPE
    }, status = AUTH)
    @PutMapping(value = "/{questionId}/status")
    public ApiResponseDto<Long> convertStatus(@PathVariable("questionId") Long questionId,
                                              @AuthUser Member member) {
        questionCommandService.convertStatus(questionId, member);
        return ApiResponseDto.onSuccess(questionId);
    }


    @Operation(summary = "대표 질문 조회", description = "대표 사이렌을 조회합니다. 미해결 인기순으로 정렬하고, 상단 3개의 사이렌을 반환합니다.")
    @ApiErrorCodeExample(value = {})
    @GetMapping("/representative")
    public ApiResponseDto<RepresentativeQuestionDto> getRepresentativeQuestionList() {
        List<Question> representativeQuestionList = questionQueryService.getRepresentativeQuestionList();
        RepresentativeQuestionDto listDto = QuestionConverter.toRepresentativeQuestionDto(
                representativeQuestionList);
        setRecommendCntInList(listDto.getQuestionList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 질문 목록 조회", description = "특정 사용자가 작성한 질문 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/member/{userUrl}")
    public ApiResponseDto<QuestionSummaryListDto> getQuestionsByUsername(@PathVariable("userUrl") String userUrl,
                                                                         @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.QUESTION_SIZE, latestSorting);
        Page<Question> questions = questionQueryService.getPagedQuestionListByUserUrl(userUrl, pageable);
        QuestionSummaryListDto listDto = QuestionConverter.toListDto(questions);
        setRecommendCntInList(listDto.getQuestionList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 질문 조회", description = "특정 질문의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.BOARD_NOT_FOUND
    })
    @GetMapping("/{questionId}")
    public ApiResponseDto<QuestionDetailDto> getQuestionByBoardId(@PathVariable("questionId") Long questionId) {
        Question questionByBoardId = questionQueryService.getQuestionByBoardId(questionId);
        QuestionDetailDto detailDto = QuestionConverter.toDetailDto(questionByBoardId);
        detailDto.setViewCount(questionCacheService.applyViewCountToRedis(questionId));
        detailDto.setRecommendCount(recommendQueryService.countRecommend(questionId));
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "질문 검색 및 정렬", description = "키워드를 포함하고 있는 해시태그, 혹은 내용을 지닌 질문을 조회합니다." +
            "이때 정렬 파라미터를 통해 검색 결과를 정렬합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.BOARD_SEARCHING_KEYWORD_IS_TOO_SHORT
    })
    @GetMapping("/search")
    public ApiResponseDto<QuestionSummaryListDto> searchQuestionListBySortParam(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortParam") QuestionSortParam sortParam,
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        ObjectUtil.validateKeywordLength(keyword);
        Pageable pageable = PageRequest.of(currentPage, QUESTION_SIZE, latestSorting);
        Page<Question> pagedQuestionList = questionQueryService
                .getPagedQuestionListByKeywordAndSortParam(keyword, sortParam, pageable);
        QuestionSummaryListDto listDto = QuestionConverter.toListDto(pagedQuestionList);
        setRecommendCntInList(listDto.getQuestionList());
        return ApiResponseDto.onSuccess(listDto);
    }


    @Operation(summary = "질문 삭제 🔑", description = "특정 질문을 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등 모두 삭제됩니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.BOARD_CANNOT_EDIT_OTHERS,
            ErrorStatus.BOARD_NOT_FOUND
    }, status = AUTH)
    @DeleteMapping("/{questionId}")
    public ApiResponseDto<Boolean> deleteQuestion(@PathVariable("questionId") Long questionId,
                                                  @AuthUser Member member) {
        questionCommandService.deleteQuestionWithRelations(questionId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "질문 강제 삭제 🔑", description = "특정 질문이 관리자에 의해 삭제됩니다. 게시글과 관련된 댓글, 대댓글, 미디어 등 모두 삭제됩니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.AUTH_ROLE_CANNOT_EXECUTE_URI,
            ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION,
            ErrorStatus.BOARD_NOT_FOUND
    }, status = AUTH)
    @DeleteMapping("/{questionId}/admin")
    public ApiResponseDto<Boolean> deleteQuestionByAdmin(@PathVariable("questionId") Long questionId,
                                                         @AuthUser Member admin) {
        questionCommandService.deleteQuestionByAdmin(questionId, admin);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    private void setRecommendCntInList(List<QuestionSummaryDto> questionList) {
        questionList
                .forEach(question ->
                        question.setRecommendCount(
                                recommendQueryService.countRecommend(question.getBoardId()
                                )));
    }

    // TODO 사용자가 작성한 대답과 관련된 question list 가져오기
}

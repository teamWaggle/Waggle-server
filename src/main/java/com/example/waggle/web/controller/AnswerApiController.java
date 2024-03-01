package com.example.waggle.web.controller;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.answer.service.AnswerQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.AnswerConverter;
import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.answer.AnswerResponse.AnswerListDto;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
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
@RequestMapping("/api/answers")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Answer API", description = "답변 API")
public class AnswerApiController {

    private final AnswerCommandService answerCommandService;
    private final AnswerQueryService answerQueryService;
    private final RecommendQueryService recommendQueryService;

    private final Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "답변 작성 🔑", description = "사용자가 답변을 작성합니다. 작성한 답변의 정보를 저장하고 답변의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(value = "/{questionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createAnswer(@PathVariable("questionId") Long questionId,
                                             @RequestPart("createAnswerRequest") AnswerRequest createAnswerRequest,
                                             @RequestPart(required = false, value = "files") List<MultipartFile> files,
                                             @AuthUser Member member) {
        Long answer = answerCommandService.createAnswer(questionId, createAnswerRequest, files, member);
        return ApiResponseDto.onSuccess(answer);
    }

    @Operation(summary = "답변 수정 🔑", description = "사용자가 답변을 수정합니다. 수정한 답변의 정보를 저장하고 답변의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{answerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateAnswer(@PathVariable("answerId") Long answerId,
                                             @RequestPart("updateAnswerRequest") AnswerRequest updateAnswerRequest,
                                             @RequestPart("mediaUpdateRequest") MediaUpdateDto mediaUpdateRequest,
                                             @RequestPart(required = false, value = "files") List<MultipartFile> files,
                                             @AuthUser Member member) {
        mediaUpdateRequest.getMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        mediaUpdateRequest.getDeleteMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        answerCommandService.updateAnswer(answerId, updateAnswerRequest, mediaUpdateRequest, files, member);
        return ApiResponseDto.onSuccess(answerId);
    }

    @Operation(summary = "질문의 답변 목록 조회", description = "질문의 전체 답변 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/question/{questionId}")
    public ApiResponseDto<AnswerListDto> getAllAnswerByPage(@PathVariable("questionId") Long questionId,
                                                            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Answer> pagedAnswers = answerQueryService.getPagedAnswers(questionId, pageable);
        AnswerListDto listDto = AnswerConverter.toAnswerListDto(pagedAnswers);
        //recommend relation field
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 답변 목록 조회", description = "특정 사용자가 작성한 답변 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/member/{memberId}")
    public ApiResponseDto<AnswerListDto> getAnswerByMemberId(@PathVariable("memberId") Long memberId,
                                                             @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Answer> pagedAnswerByUsername = answerQueryService.getPagedAnswerByMemberId(memberId, pageable);
        AnswerListDto listDto = AnswerConverter.toAnswerListDto(pagedAnswerByUsername);
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "답변 삭제 🔑", description = "특정 답변을 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등이 모두 삭제됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{answerId}")
    public ApiResponseDto<Boolean> deleteAnswer(@PathVariable("answerId") Long answerId,
                                                @AuthUser Member member) {
        answerCommandService.deleteAnswer(answerId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}

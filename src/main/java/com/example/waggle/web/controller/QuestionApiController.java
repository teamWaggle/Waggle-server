package com.example.waggle.web.controller;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.question.service.QuestionQueryService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.QuestionConverter;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.question.QuestionRequest;
import com.example.waggle.web.dto.question.QuestionResponse;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/questions")
@RestController
@Tag(name = "Question API", description = "질문 API")
public class QuestionApiController {

    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private final RecommendQueryService recommendQueryService;
    private final Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "질문 작성", description = "사용자가 질문을 작성합니다. 작성한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "질문 작성 성공. 작성한 질문의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 질문 작성에 실패했습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createQuestion(@RequestPart QuestionRequest.Post request,
                                               @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) throws IOException {
        Long boardId = questionCommandService.createQuestion(request, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "질문 수정", description = "사용자가 질문 수정합니다. 수정한 질문의 정보를 저장하고 질문의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "질문 수정 성공. 수정한 질문의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 질문 수정에 실패했습니다.")
    @PutMapping(value = "/{boardId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateQuestion(@PathVariable Long boardId,
                                               @RequestPart QuestionRequest.Put questionUpdateDto,
                                               @RequestPart MediaRequest.Put mediaUpdateDto,
                                               @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) throws IOException {
        questionCommandService.updateQuestionV2(boardId, questionUpdateDto, mediaUpdateDto, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "전체 질문 목록 조회", description = "전체 질문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "질문 조회 성공. 전체 질문 목록을 반환합니다.")
    @GetMapping
    public ApiResponseDto<QuestionResponse.ListDto> getAllQuestions(
            @RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Question> questions = questionQueryService.getPagedQuestions(pageable);
        QuestionResponse.ListDto listDto = QuestionConverter.toListDto(questions);
        listDto.getQuestionsList().stream()
                .forEach(q -> {
                    q.setRecommendIt(recommendQueryService.checkRecommend(q.getId(), q.getUsername()));
                    q.setRecommendCount(recommendQueryService.countRecommend(q.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 질문 목록 조회", description = "특정 사용자가 작성한 질문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "질문 조회 성공. 사용자가 작성한 질문 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{username}")
    public ApiResponseDto<QuestionResponse.ListDto> getQuestionsByUsername(
            @RequestParam(defaultValue = "0") int currentPage, @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Question> questions = questionQueryService.getPagedQuestionsByUsername(username,
                pageable);
        QuestionResponse.ListDto listDto = QuestionConverter.toListDto(questions);
        listDto.getQuestionsList().stream()
                .forEach(q -> {
                    q.setRecommendIt(recommendQueryService.checkRecommend(q.getId(), q.getUsername()));
                    q.setRecommendCount(recommendQueryService.countRecommend(q.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 질문 조회", description = "특정 질문의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "질문 조회 성공. 특정 질문의 상세 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음. 지정된 질문 ID에 해당하는 질문을 찾을 수 없습니다.")
    @GetMapping("/{boardId}")
    public ApiResponseDto<QuestionResponse.DetailDto> getQuestionByBoardId(@PathVariable Long boardId) {
        Question questionByBoardId = questionQueryService.getQuestionByBoardId(boardId);
        QuestionResponse.DetailDto detailDto = QuestionConverter.toDetailDto(questionByBoardId);
        detailDto.setRecommendIt(recommendQueryService.checkRecommend(detailDto.getId(), detailDto.getUsername()));
        detailDto.setRecommendCount(recommendQueryService.countRecommend(detailDto.getId()));
        return ApiResponseDto.onSuccess(detailDto);
    }

    //TODO 사용자가 작성한 대답과 관련된 question list 가져오기
}

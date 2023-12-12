package com.example.waggle.web.controller;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.answer.service.AnswerQueryService;
import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.AnswerConverter;
import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.answer.AnswerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/answers")
@RestController
@Tag(name = "Answer API", description = "대답 API")
public class AnswerApiController {

    private final AnswerCommandService answerCommandService;
    private final AnswerQueryService answerQueryService;
    private final RecommendQueryService recommendQueryService;
    private final AwsS3Service awsS3Service;
    private final Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "대답 작성", description = "사용자가 대답 작성합니다. 작성한 대답의 정보를 저장하고 대답의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "대답 작성 성공. 작성한 대답의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 질문 작성에 실패했습니다.")
    @PostMapping("/{questionId}")
    public ApiResponseDto<Long> createAnswer(@RequestPart AnswerRequest.Post request,
                                             @RequestPart List<MultipartFile> multipartFiles,
                                             @PathVariable Long questionId) throws IOException {
        Long answer = answerCommandService.createAnswer(questionId, request, multipartFiles);
        return ApiResponseDto.onSuccess(answer);
    }

    @Operation(summary = "대답 수정", description = "사용자가 대답을 수정합니다. 수정한 대답의 정보를 저장하고 대답의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "대답 수정 성공. 수정한 대답의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 대답 수정에 실패했습니다.")
    @PutMapping("/{boardId}")
    public ApiResponseDto<Long> updateAnswer(@PathVariable Long boardId,
                                               @RequestPart List<MultipartFile> multipartFiles,
                                               @RequestPart List<String> deleteFiles,
                                               @RequestPart AnswerRequest.Post request) throws IOException{
        answerCommandService.updateAnswer(boardId, request, multipartFiles, deleteFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "질문의 대답 목록 조회", description = "질문의 전체 대답 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "대답 조회 성공. 질문의 전체 대답 목록을 반환합니다.")
    @GetMapping
    public ApiResponseDto<AnswerResponse.ListDto> getAllAnswerByPage(
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam Long questionId) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Answer> pagedAnswers = answerQueryService.getPagedAnswers(questionId, pageable);
        AnswerResponse.ListDto listDto = AnswerConverter.toListDto(pagedAnswers);
        //recommend relation field
        listDto.getAnswerList().stream()
                .forEach(a->{
                    a.setRecommendIt(recommendQueryService.checkRecommend(a.getId(),a.getUsername()));
                    a.setRecommendCount(recommendQueryService.countRecommend(a.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 대답 목록 조회", description = "특정 사용자가 작성한 대답 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "대답 조회 성공. 사용자가 작성한 대답 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{username}")
    public ApiResponseDto<AnswerResponse.ListDto> getAnswerByUsername(
            @RequestParam(defaultValue = "0") int currentPage, @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Answer> pagedAnswerByUsername = answerQueryService.getPagedAnswerByUsername(username, pageable);
        AnswerResponse.ListDto listDto = AnswerConverter.toListDto(pagedAnswerByUsername);

        listDto.getAnswerList().stream()
                .forEach(a->{
                    a.setRecommendIt(recommendQueryService.checkRecommend(a.getId(),a.getUsername()));
                    a.setRecommendCount(recommendQueryService.countRecommend(a.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }
}

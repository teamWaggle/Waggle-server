package com.example.waggle.web.controller;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.StoryConverter;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.story.StoryRequest;
import com.example.waggle.web.dto.story.StoryResponse.StoryDetailDto;
import com.example.waggle.web.dto.story.StoryResponse.StorySummaryListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/stories")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Story API", description = "스토리 API")
public class StoryApiController {
    private final StoryCommandService storyCommandService;
    private final StoryQueryService storyQueryService;
    private final RecommendQueryService recommendQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "스토리 작성 🔑", description = "사용자가 스토리를 작성합니다. 작성한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createStory(@RequestPart("createStoryRequest") StoryRequest createStoryRequest,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        Long boardId = storyCommandService.createStory(createStoryRequest, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "스토리 수정 🔑", description = "사용자가 스토리를 수정합니다. 수정한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{storyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateStory(@PathVariable("storyId") Long storyId,
                                            @RequestPart("updateStoryRequest") StoryRequest updateStoryRequest,
                                            @RequestPart("updateMediaRequest") MediaUpdateDto updateMediaRequest,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        updateMediaRequest.getMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        updateMediaRequest.getDeleteMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        storyCommandService.updateStoryV2(storyId, updateStoryRequest, updateMediaRequest, multipartFiles);
        return ApiResponseDto.onSuccess(storyId);
    }


    @Operation(summary = "전체 스토리 목록 조회", description = "전체 스토리 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping
    public ApiResponseDto<StorySummaryListDto> getAllStories(
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStories(pageable)));
    }

    @Operation(summary = "사용자의 스토리 목록 조회", description = "특정 사용자가 작성한 스토리 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/member/{memberId}")
    public ApiResponseDto<StorySummaryListDto> getStoriesByUsername(@PathVariable("memberId") Long memberId,
                                                                    @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStoriesByMemberId(memberId, pageable)));
    }

    @Operation(summary = "특정 스토리 조회", description = "특정 스토리의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{storyId}")
    public ApiResponseDto<StoryDetailDto> getStoryByBoardId(@PathVariable("storyId") Long storyId) {
        Story storyByBoardId = storyQueryService.getStoryByBoardId(storyId);
        StoryDetailDto detailDto = StoryConverter.toDetailDto(storyByBoardId);
        recommendQueryService.getRecommendValues(detailDto);
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "스토리 삭제 🔑", description = "특정 스토리를 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{storyId}")
    public ApiResponseDto<Boolean> deleteStory(@PathVariable("storyId") Long storyId) {
        storyCommandService.deleteStory(storyId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}

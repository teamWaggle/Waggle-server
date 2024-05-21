package com.example.waggle.domain.board.presentation.controller;

import com.example.waggle.domain.board.application.story.StoryCommandService;
import com.example.waggle.domain.board.application.story.StoryQueryService;
import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.converter.StoryConverter;
import com.example.waggle.domain.board.presentation.dto.story.StoryRequest;
import com.example.waggle.domain.board.presentation.dto.story.StoryResponse.StoryDetailDto;
import com.example.waggle.domain.board.presentation.dto.story.StoryResponse.StorySummaryListDto;
import com.example.waggle.domain.board.presentation.dto.story.StorySortParam;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.application.query.RecommendQueryService;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ApiResponseDto<Long> createStory(@RequestPart("createStoryRequest") @Valid StoryRequest createStoryRequest,
                                            @AuthUser Member member) {
        List<String> removedPrefixMedia = createStoryRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        createStoryRequest.setMediaList(removedPrefixMedia);
        Long boardId = storyCommandService.createStory(createStoryRequest, member);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "스토리 수정 🔑", description = "사용자가 스토리를 수정합니다. 수정한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{storyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateStory(@PathVariable("storyId") Long storyId,
                                            @RequestPart("updateStoryRequest") StoryRequest updateStoryRequest,
                                            @AuthUser Member member) {
        List<String> removedPrefixMedia = updateStoryRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        updateStoryRequest.setMediaList(removedPrefixMedia);
        storyCommandService.updateStory(storyId, updateStoryRequest, member);
        return ApiResponseDto.onSuccess(storyId);
    }


    @Operation(summary = "전체 스토리 목록 조회", description = "전체 스토리 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping
    public ApiResponseDto<StorySummaryListDto> getAllStories(
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.STORY_SIZE, latestSorting);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStories(pageable)));
    }

    @Operation(summary = "스토리 정렬 조회", description = "필터 옵션에 맞추어 결과를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/sort")
    public ApiResponseDto<StorySummaryListDto> getStoryByFilter(
            @RequestParam(name = "sortParam") StorySortParam sortParam,
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.STORY_SIZE);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStoriesBySortParam(sortParam, pageable)));
    }

    @Operation(summary = "사용자의 스토리 목록 조회", description = "특정 사용자가 작성한 스토리 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/member/{userUrl}")
    public ApiResponseDto<StorySummaryListDto> getStoriesByUsername(@PathVariable("userUrl") String userUrl,
                                                                    @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.STORY_SIZE, latestSorting);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStoriesByUserUrl(userUrl, pageable)));
    }

    @Operation(summary = "특정 스토리 조회", description = "특정 스토리의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{storyId}")
    public ApiResponseDto<StoryDetailDto> getStoryByBoardId(@PathVariable("storyId") Long storyId) {
        Story storyByBoardId = storyQueryService.getStoryByBoardId(storyId);
        StoryDetailDto detailDto = StoryConverter.toDetailDto(storyByBoardId);
        detailDto.setRecommendCount(recommendQueryService.countRecommend(storyId));
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "스토리 삭제 🔑", description = "특정 스토리를 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{storyId}")
    public ApiResponseDto<Boolean> deleteStory(@PathVariable("storyId") Long storyId,
                                               @AuthUser Member member) {
        storyCommandService.deleteStoryWithRelations(storyId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "스토리 강제 삭제 🔑", description = "특정 스토리가 관리자에 의해 삭제됩니다. 게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{storyId}/admin")
    public ApiResponseDto<Boolean> deleteStoryByAdmin(@PathVariable("storyId") Long storyId,
                                                      @AuthUser Member member) {
        storyCommandService.deleteStoryByAdmin(storyId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}

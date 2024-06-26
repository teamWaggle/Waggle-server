package com.example.waggle.domain.board.presentation.controller;

import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.ADMIN;
import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.AUTH;
import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.BOARD_DATA_CHANGE;
import static com.example.waggle.global.util.PageUtil.LATEST_SORTING;
import static com.example.waggle.global.util.PageUtil.STORY_SIZE;

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
import com.example.waggle.global.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "스토리 작성 🔑", description = "사용자가 스토리를 작성합니다. 작성한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEDIA_PREFIX_IS_WRONG
    }, status = AUTH)
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
    @ApiErrorCodeExample(status = BOARD_DATA_CHANGE)
    @PutMapping(value = "/{storyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateStory(@PathVariable("storyId") Long storyId,
                                            @RequestPart("updateStoryRequest") @Valid StoryRequest updateStoryRequest,
                                            @AuthUser Member member) {
        List<String> removedPrefixMedia = updateStoryRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        updateStoryRequest.setMediaList(removedPrefixMedia);
        storyCommandService.updateStory(storyId, updateStoryRequest, member);
        return ApiResponseDto.onSuccess(storyId);
    }

    @Operation(summary = "사용자의 스토리 목록 조회", description = "특정 사용자가 작성한 스토리 목록을 조회합니다.")
    @ApiErrorCodeExample
    @GetMapping("/member/{userUrl}")
    public ApiResponseDto<StorySummaryListDto> getStoriesByUsername(@PathVariable("userUrl") String userUrl,
                                                                    @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, STORY_SIZE, LATEST_SORTING);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStoryListByUserUrl(userUrl, pageable)));
    }

    @Operation(summary = "특정 스토리 조회", description = "특정 스토리의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.BOARD_NOT_FOUND
    })
    @GetMapping("/{storyId}")
    public ApiResponseDto<StoryDetailDto> getStoryByBoardId(@PathVariable("storyId") Long storyId) {
        Story storyByBoardId = storyQueryService.getStoryByBoardId(storyId);
        StoryDetailDto detailDto = StoryConverter.toDetailDto(storyByBoardId);
        detailDto.setRecommendCount(recommendQueryService.countRecommend(storyId));
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "스토리 검색 및 정렬", description = "키워드를 포함하고 있는 해시태그, 혹은 내용을 지닌 스토리를 조회합니다." +
            "이때 정렬 파라미터를 통해 검색 결과를 정렬합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.SEARCHING_KEYWORD_IS_TOO_SHORT
    })
    @GetMapping("/search")
    public ApiResponseDto<StorySummaryListDto> searchStoryListBySorting(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortParam") StorySortParam sortParam,
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        ObjectUtil.validateKeywordLength(keyword);
        Pageable pageable = PageRequest.of(currentPage, STORY_SIZE, LATEST_SORTING);
        Page<Story> PagedStoryList = storyQueryService.getPagedStoryListByKeywordAndSortParam(keyword, sortParam,
                pageable);
        return ApiResponseDto.onSuccess(StoryConverter.toListDto(PagedStoryList));
    }

    @Operation(summary = "스토리 삭제 🔑", description = "특정 스토리를 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample(status = BOARD_DATA_CHANGE)
    @DeleteMapping("/{storyId}")
    public ApiResponseDto<Boolean> deleteStory(@PathVariable("storyId") Long storyId,
                                               @AuthUser Member member) {
        storyCommandService.deleteStoryWithRelations(storyId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "스토리 강제 삭제 🔑", description = "특정 스토리가 관리자에 의해 삭제됩니다. 게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION,
            ErrorStatus.BOARD_NOT_FOUND
    }, status = ADMIN)
    @DeleteMapping("/{storyId}/admin")
    public ApiResponseDto<Boolean> deleteStoryByAdmin(@PathVariable("storyId") Long storyId,
                                                      @AuthUser Member admin) {
        storyCommandService.deleteStoryByAdmin(storyId, admin);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}

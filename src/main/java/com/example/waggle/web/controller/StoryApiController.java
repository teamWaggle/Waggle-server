package com.example.waggle.web.controller;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.StoryConverter;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import com.example.waggle.web.dto.story.StoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
@Tag(name = "Story API", description = "스토리 API")
public class StoryApiController {
    private final StoryCommandService storyCommandService;
    private final StoryQueryService storyQueryService;
    private final RecommendQueryService recommendQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "스토리 작성", description = "사용자가 스토리를 작성합니다. 작성한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 작성 성공. 작성한 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 작성에 실패했습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createStory(@RequestPart StoryRequest.Post request,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles,
                                            @AuthUser Member member) {
        log.info("member = {}, {}", member.getEmail(), member.getUsername());
        Long boardId = storyCommandService.createStory(member, request, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "스토리 수정", description = "사용자가 스토리를 수정합니다. 수정한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 수정 성공. 수정한 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 수정에 실패했습니다.")
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateStory(@PathVariable Long boardId,
                                            @RequestPart StoryRequest.Post request,
                                            @RequestPart MediaRequest.Put mediaUpdateDto,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles,
                                            @AuthUser Member member) {
        mediaUpdateDto.getMediaList().forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        mediaUpdateDto.getDeleteMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        storyCommandService.updateStory(boardId, member, request, mediaUpdateDto, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }


    @Operation(summary = "전체 스토리 목록 조회", description = "전체 스토리 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 조회 성공. 전체 스토리 목록을 반환합니다.")
    @GetMapping
    public ApiResponseDto<StoryResponse.ListDto> getAllStories(@RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStories(pageable)));
    }

    @Operation(summary = "사용자의 스토리 목록 조회", description = "특정 사용자가 작성한 스토리 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 조회 성공. 사용자가 작성한 스토리 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{memberId}")
    public ApiResponseDto<StoryResponse.ListDto> getStoriesByUsername(@RequestParam(defaultValue = "0") int currentPage,
                                                                      @PathVariable Long memberId) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        return ApiResponseDto.onSuccess(
                StoryConverter.toListDto(storyQueryService.getPagedStoriesByMemberId(memberId, pageable)));
    }

    @Operation(summary = "특정 스토리 조회", description = "특정 스토리의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 조회 성공. 특정 스토리의 상세 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "스토리를 찾을 수 없음. 지정된 스토리 ID에 해당하는 스토리를 찾을 수 없습니다.")
    @GetMapping("/{boardId}")
    public ApiResponseDto<StoryResponse.DetailDto> getStoryByBoardId(@PathVariable Long boardId) {
        Story storyByBoardId = storyQueryService.getStoryByBoardId(boardId);
        StoryResponse.DetailDto detailDto = StoryConverter.toDetailDto(storyByBoardId);
        recommendQueryService.getRecommendValues(detailDto);
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "스토리 삭제", description = "특정 스토리를 삭제합니다. 게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "스토리를 찾을 수 없거나 인증 정보가 스토리를 작성한 유저와 일치하지 않습니다.")
    @DeleteMapping
    public ApiResponseDto<Boolean> deleteStory(@RequestParam("boardId") Long boardId,
                                               @AuthUser Member member) {
        storyCommandService.deleteStory(boardId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}

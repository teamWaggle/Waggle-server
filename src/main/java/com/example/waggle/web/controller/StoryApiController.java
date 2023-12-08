package com.example.waggle.web.controller;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.StoryConverter;
import com.example.waggle.web.dto.story.StoryRequest;
import com.example.waggle.web.dto.story.StoryResponse;
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
@RequestMapping("/api/stories")
@RestController
@Tag(name = "Story API", description = "스토리 API")
public class StoryApiController {
    private final StoryCommandService storyCommandService;
    private final StoryQueryService storyQueryService;
    private final RecommendQueryService recommendQueryService;
    private final AwsS3Service awsS3Service;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "스토리 작성", description = "사용자가 스토리를 작성합니다. 작성한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 작성 성공. 작성한 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 작성에 실패했습니다.")
    @PostMapping
    public ApiResponseDto<Long> createStory(@RequestPart StoryRequest.Post request,
                                            @RequestPart List<MultipartFile> multipartFiles,
                                            @RequestPart MultipartFile thumbnail) throws IOException {
        List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);
        String uploadThumbnail = awsS3Service.uploadFile(thumbnail);
        Long boardId = storyCommandService.createStory(request);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "스토리 수정", description = "사용자가 스토리를 수정합니다. 수정한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 수정 성공. 수정한 스토리의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 수정에 실패했습니다.")
    @PutMapping("/{boardId}")
    public ApiResponseDto<Long> updateStory(@PathVariable Long boardId,
                                            @ModelAttribute StoryRequest.Post storyWriteDto,
                                            @RequestPart List<MultipartFile> multipartFiles,
                                            @RequestPart MultipartFile thumbnail) throws IOException {
        List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);
        String uploadThumbnail = awsS3Service.uploadFile(thumbnail);
        storyCommandService.updateStory(boardId, storyWriteDto);
        return ApiResponseDto.onSuccess(boardId);
    }


    @Operation(summary = "전체 스토리 목록 조회", description = "전체 스토리 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 조회 성공. 전체 스토리 목록을 반환합니다.")
    @GetMapping
    public ApiResponseDto<StoryResponse.ListDto> getAllStories(@RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Story> pagedStories = storyQueryService.getPagedStories(pageable);
        StoryResponse.ListDto listDto = StoryConverter.toListDto(pagedStories);
        listDto.getStoryList().stream()
                .forEach(s->{
                    s.setRecommendIt(recommendQueryService.checkRecommend(s.getId(),s.getUsername()));
                    s.setRecommendCount(recommendQueryService.countRecommend(s.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 스토리 목록 조회", description = "특정 사용자가 작성한 스토리 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 조회 성공. 사용자가 작성한 스토리 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{username}")
    public ApiResponseDto<StoryResponse.ListDto> getStoriesByUsername(@RequestParam(defaultValue = "0") int currentPage,
                                                                      @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Story> pagedStories = storyQueryService.getPagedStoriesByUsername(username, pageable);
        StoryResponse.ListDto listDto = StoryConverter.toListDto(pagedStories);
        listDto.getStoryList().stream()
                .forEach(s->{
                    s.setRecommendIt(recommendQueryService.checkRecommend(s.getId(),s.getUsername()));
                    s.setRecommendCount(recommendQueryService.countRecommend(s.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 스토리 조회", description = "특정 스토리의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 조회 성공. 특정 스토리의 상세 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "스토리를 찾을 수 없음. 지정된 스토리 ID에 해당하는 스토리를 찾을 수 없습니다.")
    @GetMapping("/{boardId}")
    public ApiResponseDto<StoryResponse.DetailDto> getStoryByBoardId(@PathVariable Long boardId) {
        Story storyByBoardId = storyQueryService.getStoryByBoardId(boardId);
        StoryResponse.DetailDto detailDto = StoryConverter.toDetailDto(storyByBoardId);
        detailDto.setRecommendIt(recommendQueryService.checkRecommend(detailDto.getId(), detailDto.getUsername()));
        detailDto.setRecommendCount(recommendQueryService.countRecommend(detailDto.getId()));
        return ApiResponseDto.onSuccess(detailDto);
    }
}

package com.example.waggle.board.story.controller;

import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.board.story.service.StoryService;
import com.example.waggle.commons.dto.page.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/story")
@Tag(name = "Story API", description = "스토리 API")
public class StoryApiController {

    private final StoryService storyService;
    private final FileStore fileStore;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(
            summary = "스토리 작성",
            description = "사용자가 스토리를 작성합니다. 작성한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 작성 성공. 작성한 스토리의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 작성에 실패했습니다."
    )
    @PostMapping("/write")
    public ResponseEntity<?> singleStoryWrite(@RequestPart StoryWriteDto storyWriteDto,
                                              @RequestPart List<MultipartFile> multipartFiles,
                                              @RequestPart MultipartFile thumbnail,
                                              BindingResult bindingResult) throws IOException {
        Long boardId = storyService.createStory(storyWriteDto, multipartFiles, thumbnail);
        return ResponseEntity.ok(boardId);
    }

    @Operation(
            summary = "스토리 수정",
            description = "사용자가 스토리를 수정합니다. 수정한 스토리의 정보를 저장하고 스토리의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 수정 성공. 수정한 스토리의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스토리 수정에 실패했습니다."
    )
    @PostMapping("/edit/{boardId}")
    public ResponseEntity<?> singleStoryEdit(@ModelAttribute StoryWriteDto storyDto,
                                             @RequestPart List<MultipartFile> multipartFiles,
                                             @RequestPart MultipartFile thumbnail,
                                             @PathVariable Long boardId) throws IOException {
        storyService.updateStory(boardId, storyDto, multipartFiles, thumbnail);
        return ResponseEntity.ok(boardId);  //TODO redirect return "redirect:/story/" + username + "/" + boardId;
    }


    @Operation(
            summary = "전체 스토리 목록 조회",
            description = "전체 스토리 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 조회 성공. 전체 스토리 목록을 반환합니다."
    )
    @GetMapping("/all")
    public ResponseEntity<?> story(@RequestParam(defaultValue = "0")int currentPage) {  // TODO 인기순, 최신순에 따른 필터링 필요
        Pageable pageable = PageRequest.of(currentPage, 10,latestSorting);
        Page<StorySummaryDto> storiesPaging = storyService.getPagedStories(pageable);

        return ResponseEntity.ok(storiesPaging);
    }

    @Operation(
            summary = "사용자의 스토리 목록 조회",
            description = "특정 사용자가 작성한 스토리 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 조회 성공. 사용자가 작성한 스토리 목록을 반환합니다."
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다."
    )
    @GetMapping("/{username}")
    public ResponseEntity<?> memberStory(@RequestParam(defaultValue = "0")int currentPage,
                                         @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10,latestSorting);
        Page<StorySummaryDto> storiesByUsername = storyService.getPagedStoriesByUsername(username, pageable);
        return ResponseEntity.ok(storiesByUsername);
    }

    @Operation(
            summary = "특정 스토리 조회",
            description = "특정 스토리의 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 조회 성공. 특정 스토리의 상세 정보를 반환합니다."
    )
    @ApiResponse(
            responseCode = "404",
            description = "스토리를 찾을 수 없음. 지정된 스토리 ID에 해당하는 스토리를 찾을 수 없습니다."
    )
    @GetMapping("/{username}/{boardId}")
    public ResponseEntity<?> singleStoryForm(@PathVariable String username,
                                             @PathVariable Long boardId) {
        StoryDetailDto storyByBoardId = storyService.getStoryByBoardId(boardId);
        return ResponseEntity.ok(storyByBoardId);
    }
}

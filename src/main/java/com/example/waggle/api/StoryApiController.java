package com.example.waggle.api;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.service.board.StoryService;
import com.example.waggle.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/story")
@Tag(name = "Story API", description = "스토리 API")
public class StoryApiController {

    private final StoryService storyService;

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
    public ResponseEntity<?> singleStoryWrite(@Validated @RequestBody StoryWriteDto storyDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            // TODO 예외 처리
        }
        String username = SecurityUtil.getCurrentUsername();
        Long boardId = storyService.saveStory(storyDto);
        return ResponseEntity.ok(boardId);  // TODO redirect return "redirect:/story/" + username + "/" + boardId;
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
    public ResponseEntity<?> singleStoryEdit(@ModelAttribute StoryWriteDto storyDto, @PathVariable Long boardId) {
        storyService.changeStory(storyDto);
        return ResponseEntity.ok(boardId);  //TODO redirect return "redirect:/story/" + username + "/" + boardId;
    }

    /**
     * remove
     */

    @Operation(
            summary = "전체 스토리 목록 조회",
            description = "전체 스토리 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 조회 성공. 전체 스토리 목록을 반환합니다."
    )
    @GetMapping("/all")
    public ResponseEntity<?> story() {  // TODO 인기순, 최신순에 따른 필터링 필요
        List<StorySimpleViewDto> allStoryByMember = storyService.findAllStory();
        log.info("allStoryByMember = {}", allStoryByMember);

        return ResponseEntity.ok(allStoryByMember);
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
    public ResponseEntity<?> memberStory(@PathVariable String username) {
        List<StorySimpleViewDto> allStoryByMember = storyService.findAllStoryByUsername(username);
        return ResponseEntity.ok(allStoryByMember);
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
    public ResponseEntity<?> singleStoryForm(@PathVariable String username, @PathVariable Long boardId) {
        StoryViewDto storyByBoardId = storyService.findStoryViewByBoardId(boardId);
        return ResponseEntity.ok(storyByBoardId);
    }
}

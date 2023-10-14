package com.example.waggle.board.helpU.controller;

import com.example.waggle.board.helpU.dto.HelpUDetailDto;
import com.example.waggle.board.helpU.dto.HelpUSummaryDto;
import com.example.waggle.board.helpU.dto.HelpUWriteDto;
import com.example.waggle.board.helpU.service.HelpUService;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.dto.page.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/help-u")
@Controller
public class HelpUApiController {

    private final HelpUService helpUService;
    private final FileStore fileStore;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(
            summary = "헬퓨 작성",
            description = "사용자가 헬퓨를 작성합니다. 작성한 헬퓨의 정보를 저장하고 헬퓨의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "헬퓨 작성 성공. 작성한 헬퓨의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 헬퓨 작성에 실패했습니다."
    )
    @PostMapping("/write")
    public ResponseEntity<?> singleHelpUWrite(@RequestPart HelpUWriteDto helpUWriteDto, @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail, BindingResult bindingResult) throws IOException {
        try {
            UploadFile uploadFile = fileStore.storeFile(thumbnail);
            String storeFileName = uploadFile.getStoreFileName();
            Long helpUId = helpUService.createHelpUWithThumbnail(helpUWriteDto, storeFileName);
            return ResponseEntity.ok(helpUId);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "헬퓨 수정",
            description = "사용자가 헬퓨를 수정합니다. 수정한 헬퓨의 정보를 저장하고 헬퓨의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "헬퓨 수정 성공. 수정한 헬퓨의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 헬퓨의 수정에 실패했습니다."
    )
    @PostMapping("/edit/{boardId}")
    public ResponseEntity<?> singleHelpUEdit(@ModelAttribute HelpUWriteDto helpUDto, @PathVariable Long boardId) {
        helpUService.updateHelpU(boardId, helpUDto);
        return ResponseEntity.ok(boardId);  //TODO redirect return "redirect:/story/" + username + "/" + boardId;
    }


    @Operation(
            summary = "전체 헬퓨 목록 조회",
            description = "전체 헬퓨 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "헬퓨 조회 성공. 전체 헬퓨 목록을 반환합니다."
    )
    @GetMapping("/all")
    public ResponseEntity<?> helpU(@RequestParam(defaultValue = "0") int currentPage) {  // TODO 인기순, 최신순에 따른 필터링 필요
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<HelpUSummaryDto> allHelpU = helpUService.getPagedHelpUs(pageable);

        return ResponseEntity.ok(allHelpU);
    }

    @Operation(
            summary = "사용자의 헬퓨 목록 조회",
            description = "특정 사용자가 작성한 헬퓨 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "헬퓨 조회 성공. 사용자가 작성한 헬퓨 목록을 반환합니다."
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다."
    )
    @GetMapping("/{username}")
    public ResponseEntity<?> memberHelpU(@RequestParam(defaultValue = "1")int currentPage, @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10,latestSorting);
        Page<HelpUSummaryDto> helpUsByUsername = helpUService.getPagedHelpUsByUsername(username, pageable);
        return ResponseEntity.ok(helpUsByUsername);
    }

    @Operation(
            summary = "특정 헬퓨 조회",
            description = "특정 헬퓨의 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "헬퓨 조회 성공. 특정 헬퓨의 상세 정보를 반환합니다."
    )
    @ApiResponse(
            responseCode = "404",
            description = "헬퓨를 찾을 수 없음. 지정된 헬퓨 ID에 해당하는 헬퓨를 찾을 수 없습니다."
    )
    @GetMapping("/{username}/{boardId}")
    public ResponseEntity<?> singleHelpUForm(@PathVariable String username, @PathVariable Long boardId) {
        HelpUDetailDto helpUByBoardId = helpUService.getHelpUByBoardId(boardId);
        return ResponseEntity.ok(helpUByBoardId);
    }
}

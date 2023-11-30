package com.example.waggle.domain.recommend.controller;

import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.domain.recommend.service.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/recommends")
@RestController
@Tag(name = "Recommend API", description = "추천 API")
public class RecommendApiController {
    private final RecommendService recommendService;

    @Operation(summary = "스토리 좋아요 혹은 취소", description = "사용자가 좋아요 버튼을 누릅니다(추가 혹은 취소). 게시글의 좋아요 수가 추가되거나 감소됩니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 추가 혹은 취소 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 자신의 게시글에는 좋아요를 누를 수 없습니다.")
    @PostMapping("/story/{boardId}")
    public ResponseEntity<?> recommendStory(@PathVariable Long boardId) {
        recommendService.handleRecommendation(boardId, BoardType.STORY);
        return ResponseEntity.ok(boardId);
    }

    @Operation(summary = "질문 게시글 좋아요 혹은 취소", description = "사용자가 좋아요 버튼을 누릅니다(추가 혹은 취소). 게시글의 좋아요 수가 추가되거나 감소됩니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 추가 혹은 취소 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 자신의 게시글에는 좋아요를 누를 수 없습니다.")
    @PostMapping("/question/{boardId}")
    public ResponseEntity<?> recommendQuestion(@PathVariable Long boardId) {
        recommendService.handleRecommendation(boardId, BoardType.QUESTION);
        return ResponseEntity.ok(boardId);
    }

    @Operation(summary = "대답 게시글 좋아요 혹은 취소", description = "사용자가 좋아요 버튼을 누릅니다(추가 혹은 취소). 게시글의 좋아요 수가 추가되거나 감소됩니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 추가 혹은 취소 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 자신의 게시글에는 좋아요를 누를 수 없습니다.")
    @PostMapping("/answer/{boardId}")
    public ResponseEntity<?> recommendAnswer(@PathVariable Long boardId) {
        recommendService.handleRecommendation(boardId, BoardType.ANSWER);
        return ResponseEntity.ok(boardId);
    }
}

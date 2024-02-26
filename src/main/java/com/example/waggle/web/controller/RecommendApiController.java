package com.example.waggle.web.controller;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.service.RecommendCommandService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/recommends")
@RestController
@Tag(name = "Recommend API", description = "추천 API")
public class RecommendApiController {
    private final RecommendCommandService recommendCommandService;
    private final RecommendQueryService recommendQueryService;

    @Operation(summary = "게시글 좋아요 혹은 취소 🔑", description = "사용자가 좋아요 버튼을 누릅니다(추가 혹은 취소). 게시글의 좋아요 수가 추가되거나 감소됩니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 추가 혹은 취소 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 자신의 게시글에는 좋아요를 누를 수 없습니다.")
    @PostMapping("/{boardId}")
    public ApiResponseDto<Long> recommendStory(@PathVariable("boardId") Long boardId) {
        recommendCommandService.handleRecommendation(boardId);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "게시글 좋아요를 누른 사람들 목록 확인", description = "해당 게시글의 좋아요를 클릭한 사람의 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 활성화 멤버 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청.")
    @GetMapping("/{boardId}")
    public ApiResponseDto<List<MemberSummaryDto>> findRecommendingMembers(@PathVariable("boardId") Long boardId) {
        List<Member> recommendingMembers = recommendQueryService.getRecommendingMembers(boardId);
        List<MemberSummaryDto> collect = recommendingMembers.stream()
                .map(MemberConverter::toMemberSummaryDto).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }
}

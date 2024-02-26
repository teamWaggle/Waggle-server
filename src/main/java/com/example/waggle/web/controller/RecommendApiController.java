package com.example.waggle.web.controller;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.service.RecommendCommandService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/recommends")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Recommend API", description = "추천 API")
public class RecommendApiController {
    private final RecommendCommandService recommendCommandService;
    private final RecommendQueryService recommendQueryService;

    @Operation(summary = "게시글 좋아요 혹은 취소 🔑", description = "사용자가 좋아요 버튼을 누릅니다(추가 혹은 취소). 게시글의 좋아요 수가 추가되거나 감소됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/{boardId}")
    public ApiResponseDto<Long> recommendStory(@PathVariable("boardId") Long boardId) {
        recommendCommandService.handleRecommendation(boardId);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "게시글 좋아요를 누른 사람들 목록 확인", description = "해당 게시글의 좋아요를 클릭한 사람의 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{boardId}")
    public ApiResponseDto<List<MemberSummaryDto>> findRecommendingMembers(@PathVariable("boardId") Long boardId) {
        List<Member> recommendingMembers = recommendQueryService.getRecommendingMembers(boardId);
        List<MemberSummaryDto> collect = recommendingMembers.stream()
                .map(MemberConverter::toMemberSummaryDto).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }
}

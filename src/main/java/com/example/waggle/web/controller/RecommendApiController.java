package com.example.waggle.web.controller;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.service.RecommendCommandService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.domain.recommend.service.RecommendSyncService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/recommends")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Recommend API", description = "추천 API")
public class RecommendApiController {
    private final RecommendCommandService recommendCommandService;
    private final RecommendQueryService recommendQueryService;
    private final RecommendSyncService recommendSyncService;

    @Operation(summary = "게시글 좋아요 혹은 취소 🔑", description = "사용자가 좋아요 버튼을 누릅니다(추가 혹은 취소). 게시글의 좋아요 수가 추가되거나 감소됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/{boardId}")
    public ApiResponseDto<Long> recommendBoard(@PathVariable("boardId") Long boardId,
                                               @AuthUser Member member) {
        recommendCommandService.handleRecommendation(boardId, member);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "게시글 좋아요를 누른 사람들 목록 확인", description = "해당 게시글의 좋아요를 클릭한 사람의 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{boardId}/memberList")
    public ApiResponseDto<MemberSummaryListDto> getRecommendingMembers(@PathVariable("boardId") Long boardId) {
        List<Member> recommendingMembers = recommendQueryService.getRecommendingMembers(boardId);
        return ApiResponseDto.onSuccess(MemberConverter.toMemberListDto(recommendingMembers));
    }

    @Operation(summary = "로그인 유저의 게시글 좋아요 유무 확인 🔑", description = "로그인 유저가 해당 게시글에 좋아요를 눌렀는지 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{boardId}")
    public ApiResponseDto<Boolean> getIsRecommend(@PathVariable("boardId") Long boardId,
                                                  @AuthUser Member member) {
        return ApiResponseDto.onSuccess(recommendQueryService.checkRecommend(boardId, member.getUsername()));
    }

    @Operation(summary = "로그인 유저 데이터베이스 -> 캐시 동기화 🔑", description = "유저의 활동을 위해 좋아요 정보를 rdb -> redis 로 옮깁니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/init")
    public ApiResponseDto<Long> initRecommendInfoInRedis(@AuthUser Member member) {
        recommendSyncService.initRecommendationInRedis(member);
        return ApiResponseDto.onSuccess(member.getId());
    }

    @Operation(summary = "캐시 -> 데이터베이스 동기화 🔑", description = "유저의 활동을 위해 좋아요 정보를 rdb -> redis 로 옮깁니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/sync")
    public ApiResponseDto<Boolean> syncRecommendInfo() {
        recommendSyncService.syncRecommendation();
        return ApiResponseDto.onSuccess(true);
    }
}

package com.example.waggle.domain.follow.presentation.controller;

import com.example.waggle.domain.follow.application.FollowCommandService;
import com.example.waggle.domain.follow.application.FollowQueryService;
import com.example.waggle.domain.follow.persistence.entity.Follow;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
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
@RestController
@RequestMapping("api/follows")
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Follow API", description = "팔로우 API")
public class FollowApiController {

    private final FollowCommandService followCommandService;
    private final FollowQueryService followQueryService;

    @Operation(summary = "팔로우 신청 🔑", description = "사용자가 다른 유저에게 팔로우를 신청합니다. 해당 유저는 사용자의 팔로잉 멤버가 됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/follow")
    public ApiResponseDto<Long> requestFollow(@RequestParam("userUrl") String userUrl,
                                              @AuthUser Member member) {
        Long follow = followCommandService.follow(member, userUrl);
        return ApiResponseDto.onSuccess(follow);
    }

    @Operation(summary = "언팔로우 신청 🔑", description = "사용자의 팔로잉 멤버를 언팔로우 신청합니다. 해당 유저는 사용자와 팔로잉 멤버에서 제외됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/unfollow")
    public ApiResponseDto<Boolean> requestUnFollow(@RequestParam("userUrl") String userUrl,
                                                   @AuthUser Member member) {
        followCommandService.unFollow(member, userUrl);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "유저 팔로잉 목록 조회", description = "로그인한 유저의 팔로잉 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/following")

    public ApiResponseDto<List<MemberSummaryDto>> getFollowingMemberListByUsername(@AuthUser Member member) {
        List<Follow> followings = followQueryService.getFollowingsByUsername(member.getUsername());
        List<MemberSummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "멤버 팔로잉 목록 조회", description = "조회한 멤버의 팔로잉 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/following/{userUrl}")
    public ApiResponseDto<List<MemberSummaryDto>> getFollowingMemberList(@PathVariable("userUrl") String userUrl) {
        List<Follow> followings = followQueryService.getFollowingsByUserUrl(userUrl);
        List<MemberSummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "유저 팔로워 목록 조회", description = "로그인한 유저의 팔로워 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/follower")
    public ApiResponseDto<List<MemberSummaryDto>> getFollowerMemberListByUsername(@AuthUser Member member) {
        List<Follow> followers = followQueryService.getFollowersByUsername(member.getUsername());
        List<MemberSummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "멤버 팔로워 목록 조회", description = "조회한 멤버의 팔로워 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/follower/{userUrl}")
    public ApiResponseDto<List<MemberSummaryDto>> getFollowerMemberList(@PathVariable("userUrl") String userUrl) {
        List<Follow> followers = followQueryService.getFollowersByUserUrl(userUrl);
        List<MemberSummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }
}

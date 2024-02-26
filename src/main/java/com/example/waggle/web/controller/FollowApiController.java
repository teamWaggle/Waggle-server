package com.example.waggle.web.controller;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.service.FollowCommandService;
import com.example.waggle.domain.follow.service.FollowQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.annotation.AuthUser;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponseDto<Long> requestFollow(@AuthUser UserDetails userDetails,
                                              @RequestParam("nickname") String nickname) {
        Long follow = followCommandService.follow(userDetails.getUsername(), nickname);
        return ApiResponseDto.onSuccess(follow);
    }

    @Operation(summary = "언팔로우 신청 🔑", description = "사용자의 팔로잉 멤버를 언팔로우 신청합니다. 해당 유저는 사용자와 팔로잉 멤버에서 제외됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/unfollow")
    public ApiResponseDto<Boolean> requestUnFollow(@AuthUser UserDetails userDetails,
                                                   @RequestParam("nickname") String nickname) {
        followCommandService.unFollow(userDetails.getUsername(), nickname);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "유저 팔로잉 목록 조회", description = "로그인한 유저의 팔로잉 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/following")
    public ApiResponseDto<List<MemberSummaryDto>> getFollowingMemberListByUsername(@AuthUser UserDetails userDetails) {
        List<Follow> followings = followQueryService.getFollowingsByUsername(userDetails.getUsername());
        List<MemberSummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "멤버 팔로잉 목록 조회", description = "조회한 멤버의 팔로잉 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/following/{memberId}")
    public ApiResponseDto<List<MemberSummaryDto>> getFollowingMemberList(@PathVariable("memberId") Long memberId) {
        List<Follow> followings = followQueryService.getFollowings(memberId);
        List<MemberSummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "유저 팔로워 목록 조회", description = "로그인한 유저의 팔로워 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/follower")
    public ApiResponseDto<List<MemberSummaryDto>> getFollowerMemberListByUsername(@AuthUser UserDetails userDetails) {
        List<Follow> followers = followQueryService.getFollowersByUsername(userDetails.getUsername());
        List<MemberSummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "멤버 팔로워 목록 조회", description = "조회한 멤버의 팔로워 목록을 보여줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/list/follower/{memberId}")
    public ApiResponseDto<List<MemberSummaryDto>> getFollowerMemberList(@PathVariable("memberId") Long memberId) {
        List<Follow> followers = followQueryService.getFollowers(memberId);
        List<MemberSummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }
}

package com.example.waggle.web.controller;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.service.FollowCommandService;
import com.example.waggle.domain.follow.service.FollowQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.security.annotation.AuthUser;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberResponse;
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
@Tag(name = "Follow API", description = "팔로우 API")
public class FollowApiController {

    private final FollowCommandService followCommandService;
    private final FollowQueryService followQueryService;

    @Operation(summary = "팔로우 신청", description = "사용자가 다른 유저에게 팔로우를 신청합니다. 해당 유저는 사용자의 팔로잉 멤버가 됩니다.")
    @ApiResponse(responseCode = "200", description = "팔로잉 멤버 추가 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 이미 팔로잉이 된 상대입니다.")
    @PostMapping("/follow")
    public ApiResponseDto<Long> requestFollow(@RequestParam Long toMember,
                                              @AuthUser Member member) {
        Long follow = followCommandService.follow(member, toMember);
        return ApiResponseDto.onSuccess(follow);
    }

    @Operation(summary = "언팔로우 신청", description = "사용자의 팔로잉 멤버를 언팔로우 신청합니다. 해당 유저는 사용자와 팔로잉 멤버에서 제외됩니다.")
    @ApiResponse(responseCode = "200", description = "언팔로우 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 팔로잉이 되지 않은 상대입니다.")
    @PostMapping("/unfollow")
    public ApiResponseDto<Boolean> requestUnFollow(@RequestParam Long toMember,
                                                   @AuthUser Member member) {
        followCommandService.unFollow(member, toMember);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "유저 팔로잉 목록 조회", description = "로그인한 유저의 팔로잉 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/following")
    public ApiResponseDto<List<MemberResponse.SummaryDto>> getFollowingMemberListByUsername() {
        List<Follow> followings = followQueryService.getFollowingsByUsername(SecurityUtil.getCurrentUsername());
        List<MemberResponse.SummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "멤버 팔로잉 목록 조회", description = "조회한 멤버의 팔로잉 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/following/{memberId}")
    public ApiResponseDto<List<MemberResponse.SummaryDto>> getFollowingMemberList(@PathVariable Long memberId) {
        List<Follow> followings = followQueryService.getFollowings(memberId);
        List<MemberResponse.SummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "유저 팔로워 목록 조회", description = "로그인한 유저의 팔로워 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/follower")
    public ApiResponseDto<List<MemberResponse.SummaryDto>> getFollowerMemberListByUsername() {
        List<Follow> followers = followQueryService.getFollowersByUsername(SecurityUtil.getCurrentUsername());
        List<MemberResponse.SummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "멤버 팔로워 목록 조회", description = "조회한 멤버의 팔로워 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/follower/{memberId}")
    public ApiResponseDto<List<MemberResponse.SummaryDto>> getFollowerMemberList(@PathVariable Long memberId) {
        List<Follow> followers = followQueryService.getFollowers(memberId);
        List<MemberResponse.SummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }
}

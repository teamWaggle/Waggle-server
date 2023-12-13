package com.example.waggle.web.controller;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.service.FollowCommandService;
import com.example.waggle.domain.follow.service.FollowQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.global.security.annotation.AuthUser;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.dto.member.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ApiResponseDto<Long> requestFollow(@RequestParam String username, @AuthUser UserDetails userDetails) {
        log.info("user = {}", userDetails.getUsername());
        Long follow = followCommandService.follow(username);
        return ApiResponseDto.onSuccess(follow);
    }

    @Operation(summary = "언팔로우 신청", description = "사용자의 팔로잉 멤버를 언팔로우 신청합니다. 해당 유저는 사용자와 팔로잉 멤버에서 제외됩니다.")
    @ApiResponse(responseCode = "200", description = "언팔로우 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 팔로잉이 되지 않은 상대입니다.")
    @PostMapping("/unfollow")
    public ApiResponseDto<Boolean> requestUnFollow(@RequestParam String username) {
        followCommandService.unFollow(username);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "유저 팔로잉 목록 조회", description = "로그인한 유저의 팔로잉 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/following")
    public ApiResponseDto<List<MemberResponse.MemberSummaryDto>> getFollowingMemberList(@AuthUser UserDetails userDetails) {
        List<Follow> followings = followQueryService.getFollowings(userDetails.getUsername());
        List<MemberResponse.MemberSummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }
    @Operation(summary = "멤버 팔로잉 목록 조회", description = "조회한 멤버의 팔로잉 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로잉 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/following/{username}")
    public ApiResponseDto<List<MemberResponse.MemberSummaryDto>> getFollowingMemberListByUsername(@PathVariable String username) {
        List<Follow> followings = followQueryService.getFollowings(username);
        List<MemberResponse.MemberSummaryDto> collect = followings.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getToMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "유저 팔로워 목록 조회", description = "로그인한 유저의 팔로워 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/follower")
    public ApiResponseDto<List<MemberResponse.MemberSummaryDto>> getFollowerMemberList() {
        List<Follow> followers = followQueryService.getFollowers(SecurityUtil.getCurrentUsername());
        List<MemberResponse.MemberSummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }

    @Operation(summary = "멤버 팔로워 목록 조회", description = "조회한 멤버의 팔로워 목록을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list/follower/{username}")
    public ApiResponseDto<List<MemberResponse.MemberSummaryDto>> getFollowerMemberListByUsername(@PathVariable String username) {
        List<Follow> followers = followQueryService.getFollowers(username);
        List<MemberResponse.MemberSummaryDto> collect = followers.stream()
                .map(f -> MemberConverter.toMemberSummaryDto(f.getFromMember())).collect(Collectors.toList());
        return ApiResponseDto.onSuccess(collect);
    }
}

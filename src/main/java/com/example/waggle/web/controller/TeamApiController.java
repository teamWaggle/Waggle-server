package com.example.waggle.web.controller;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.service.team.TeamCommandService;
import com.example.waggle.domain.schedule.service.team.TeamQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.security.annotation.AuthUser;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.TeamConverter;
import com.example.waggle.web.dto.schedule.TeamRequest.Post;
import com.example.waggle.web.dto.schedule.TeamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@RestController
@Tag(name = "Team API", description = "팀 API")
public class TeamApiController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;
    private final AwsS3Service awsS3Service;

    @Operation(summary = "팀 생성", description = "사용자가 팀을 생성합니다. 작성한 팀의 정보를 저장하고 팀의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "팀 생성 성공. 작성한 팀의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 팀 생성에 실패했습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createTeam(@Validated @RequestPart Post request,
                                           @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        request.setCoverImageUrl(MediaUtil.saveProfileImg(multipartFile, awsS3Service));
        Long createdTeamId = teamCommandService.createTeam(request);
        return ApiResponseDto.onSuccess(createdTeamId);
    }

    @Operation(summary = "팀 정보 업데이트", description = "팀의 정보를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "팀 정보 업데이트 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @PutMapping(value = "/{teamId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateTeam(@PathVariable Long teamId,
                                           @Validated @RequestPart Post request,
                                           @RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                           @RequestParam boolean allowUpload) {
        String removePrefixCoverUrl = MediaUtil.removePrefix(request.getCoverImageUrl());
        if (allowUpload) {
            awsS3Service.deleteFile(removePrefixCoverUrl);
            request.setCoverImageUrl(MediaUtil.saveProfileImg(multipartFile, awsS3Service));
        } else {
            request.setCoverImageUrl(removePrefixCoverUrl);
        }
        Long updatedTeamId = teamCommandService.updateTeam(teamId, request);
        return ApiResponseDto.onSuccess(updatedTeamId);
    }

    @Operation(summary = "팀 삭제", description = "팀을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "팀 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @DeleteMapping
    public ApiResponseDto<Boolean> deleteTeam(@RequestParam Long teamId) {
        teamCommandService.deleteTeam(teamId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

//    @Operation(summary = "팀원 추가", description = "지정된 팀에 새로운 팀원을 추가합니다.")
//    @ApiResponse(responseCode = "200", description = "팀원 추가 성공.")
//    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
//    @PostMapping("/{teamId}/members/{memberId}")
//    public ApiResponseDto<Boolean> addTeamMember(@PathVariable Long teamId, @Parameter(hidden = true) @AuthUser UserDetails userDetails) {
//        teamCommandService.addTeamMember(teamId, userDetails.getUsername());
//        return ApiResponseDto.onSuccess(Boolean.TRUE);
//    }

    @Operation(summary = "팀원 삭제(수동)", description = "리더에 의해 지정된 팀에서 특정 팀원을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "팀원 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "팀 또는 팀원을 찾을 수 없습니다.")
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ApiResponseDto<Boolean> deleteTeamMemberByLeader(@PathVariable Long teamId, @PathVariable Long memberId) {
        teamCommandService.deleteTeamMemberByLeader(teamId, memberId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀원 삭제(능동)", description = "자신이 속한 팀으로부터 탈퇴합니다.")
    @ApiResponse(responseCode = "200", description = "팀원 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "팀 또는 팀원을 찾을 수 없습니다.")
    @DeleteMapping("/{teamId}/members")
    public ApiResponseDto<Boolean> deleteTeamMemberByMyself(@PathVariable Long teamId, @AuthUser UserDetails userDetails) {
        teamCommandService.deleteTeamMemberByMyself(teamId, userDetails.getUsername());
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 리더 변경", description = "지정된 팀의 리더를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "팀 리더 변경 성공.")
    @ApiResponse(responseCode = "404", description = "팀 또는 멤버를 찾을 수 없습니다.")
    @PutMapping("/{teamId}/leader/{memberId}")
    public ApiResponseDto<Boolean> changeTeamLeader(@PathVariable Long teamId, @PathVariable Long memberId) {
        teamCommandService.changeTeamLeader(teamId, memberId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 참여 요청", description = "사용자가 팀에 참여 요청을 합니다.")
    @ApiResponse(responseCode = "200", description = "팀 참여 요청 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @PostMapping("/{teamId}/participation")
    public ApiResponseDto<Boolean> requestParticipation(@PathVariable Long teamId, @Parameter(hidden = true) @AuthUser UserDetails userDetails) {
        teamCommandService.requestParticipation(teamId, userDetails.getUsername());
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 참여 요청 승인/거절", description = "팀 리더가 팀 참여 요청을 승인하거나 거절합니다.")
    @ApiResponse(responseCode = "200", description = "팀 참여 요청 승인/거절 성공.")
    @ApiResponse(responseCode = "404", description = "팀 또는 요청을 찾을 수 없습니다.")
    @PutMapping("/{teamId}/participation/{memberId}")
    public ApiResponseDto<Boolean> respondToParticipation(@PathVariable Long teamId, @PathVariable Long memberId,
                                                          @RequestParam boolean accept) {
        teamCommandService.respondToParticipation(teamId, memberId, accept);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 조회", description = "팀의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "팀 조회 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @GetMapping("/{teamId}")
    public ApiResponseDto<TeamResponse.DetailDto> getTeam(@PathVariable Long teamId) {
        Team team = teamQueryService.getTeamById(teamId);
        return ApiResponseDto.onSuccess(TeamConverter.toDetailDto(team));
    }


    @Operation(summary = "사용자 팀 조회", description = "해당 사용자가 속한 팀 정보를 페이징하여 제공합니다.")
    @ApiResponse(responseCode = "200", description = "팀 조회 성공.")
    @ApiResponse(responseCode = "404", description = "사용자 또는 팀을 찾을 수 없습니다.")
    @GetMapping("/user/{username}/teams")
    public ApiResponseDto<TeamResponse.ListDto> getTeamsByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Team> teams = teamQueryService.getPagingTeamByUsername(username, pageable);
        return ApiResponseDto.onSuccess(TeamConverter.toSummaryListDto(teams));
    }

}

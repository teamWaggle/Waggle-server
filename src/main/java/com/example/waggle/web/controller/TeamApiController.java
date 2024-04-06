package com.example.waggle.web.controller;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.service.team.TeamCommandService;
import com.example.waggle.domain.schedule.service.team.TeamQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.TeamConverter;
import com.example.waggle.web.dto.schedule.TeamRequest;
import com.example.waggle.web.dto.schedule.TeamResponse.TeamDetailDto;
import com.example.waggle.web.dto.schedule.TeamResponse.TeamSummaryListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Team API", description = "팀 API")
public class TeamApiController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;

    @Operation(summary = "팀 생성 🔑", description = "사용자가 팀을 생성합니다. 작성한 팀의 정보를 저장하고 팀의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createTeam(@RequestPart("createTeamRequest") @Validated TeamRequest createTeamRequest,
                                           @AuthUser Member member) {
        createTeamRequest.setCoverImageUrl(MediaUtil.removePrefix(createTeamRequest.getCoverImageUrl()));
        Long createdTeamId = teamCommandService.createTeam(createTeamRequest, member);
        return ApiResponseDto.onSuccess(createdTeamId);
    }

    @Operation(summary = "팀 정보 수정 🔑", description = "팀의 정보를 업데이트합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{teamId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateTeam(@PathVariable("teamId") Long teamId,
                                           @RequestPart("updateTeamRequest") @Validated TeamRequest updateTeamRequest,
                                           @AuthUser Member member) {
        updateTeamRequest.setCoverImageUrl(MediaUtil.removePrefix(updateTeamRequest.getCoverImageUrl()));
        Long updatedTeamId = teamCommandService.updateTeam(teamId, updateTeamRequest, member);
        return ApiResponseDto.onSuccess(updatedTeamId);
    }

    @Operation(summary = "팀 삭제 🔑", description = "팀을 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{teamId}")
    public ApiResponseDto<Boolean> deleteTeam(@PathVariable("teamId") Long teamId,
                                              @AuthUser Member member) {
        teamCommandService.deleteTeam(teamId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀원 삭제(수동) 🔑", description = "리더에 의해 지정된 팀에서 특정 팀원을 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ApiResponseDto<Boolean> deleteTeamMemberByLeader(@PathVariable("teamId") Long teamId,
                                                            @PathVariable("memberId") Long memberId,
                                                            @AuthUser Member member) {
        teamCommandService.deleteTeamMemberByLeader(teamId, memberId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀원 삭제(능동) 🔑", description = "자신이 속한 팀으로부터 탈퇴합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{teamId}/members")
    public ApiResponseDto<Boolean> deleteTeamMemberByMyself(@PathVariable Long teamId,
                                                            @AuthUser Member member) {
        teamCommandService.deleteTeamMemberByMyself(teamId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 리더 변경 🔑", description = "지정된 팀의 리더를 변경합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/{teamId}/leader/{newLeaderId}")
    public ApiResponseDto<Boolean> changeTeamLeader(@PathVariable("teamId") Long teamId,
                                                    @PathVariable("newLeaderId") Long newLeaderId,
                                                    @AuthUser Member member) {
        teamCommandService.changeTeamLeader(teamId, newLeaderId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 참여 요청 승인/거절 🔑", description = "팀 리더가 팀 참여 요청을 승인하거나 거절합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/{teamId}/participation/{memberId}")
    public ApiResponseDto<Boolean> respondToParticipation(@PathVariable("teamId") Long teamId,
                                                          @PathVariable("memberId") Long memberId,
                                                          @RequestParam("accept") boolean accept,
                                                          @AuthUser Member leader) {
        teamCommandService.respondToParticipation(teamId, memberId, leader, accept);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 조회", description = "팀의 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{teamId}")
    public ApiResponseDto<TeamDetailDto> getTeam(@PathVariable("teamId") Long teamId) {
        Team team = teamQueryService.getTeamById(teamId);
        return ApiResponseDto.onSuccess(TeamConverter.toDetailDto(team));
    }

    @Operation(summary = "사용자 팀 조회", description = "해당 사용자가 속한 팀 정보를 페이징하여 제공합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/user/{memberId}/teams")
    public ApiResponseDto<TeamSummaryListDto> getTeamsByMemberId(@PathVariable("memberId") Long memberId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Team> teams = teamQueryService.getPagedTeamByMemberId(memberId, pageable);
        return ApiResponseDto.onSuccess(TeamConverter.toSummaryListDto(teams));
    }

    @Operation(summary = "팀 참여 요청 🔑", description = "사용자가 팀에 참여 요청을 합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/{teamId}/participation")
    public ApiResponseDto<Boolean> requestParticipation(@PathVariable("teamId") Long teamId,
                                                        @AuthUser Member member) {
        teamCommandService.requestParticipation(teamId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}

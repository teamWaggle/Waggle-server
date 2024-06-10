package com.example.waggle.domain.schedule.presentation.controller;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryListDto;
import com.example.waggle.domain.schedule.application.TeamCommandService;
import com.example.waggle.domain.schedule.application.TeamQueryService;
import com.example.waggle.domain.schedule.persistence.entity.Participation;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.domain.schedule.presentation.converter.TeamConverter;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamRequest;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamResponse.ParticipationStatusResponse;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamResponse.TeamDetailDto;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamResponse.TeamSummaryListDto;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.util.MediaUtil;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.ADMIN;
import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.AUTH;
import static com.example.waggle.global.util.PageUtil.TEAM_RECOMMEND_SIZE;

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
    @ApiErrorCodeExample(status = AUTH)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createTeam(@RequestPart("createTeamRequest") @Validated TeamRequest createTeamRequest,
                                           @AuthUser Member member) {
        createTeamRequest.setCoverImageUrl(MediaUtil.removePrefix(createTeamRequest.getCoverImageUrl()));
        Long createdTeamId = teamCommandService.createTeam(createTeamRequest, member);
        return ApiResponseDto.onSuccess(createdTeamId);
    }

    @Operation(summary = "팀 정보 수정 🔑", description = "팀의 정보를 업데이트합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.TEAM_LEADER_UNAUTHORIZED
    }, status = AUTH)
    @PutMapping(value = "/{teamId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateTeam(@PathVariable("teamId") Long teamId,
                                           @RequestPart("updateTeamRequest") @Validated TeamRequest updateTeamRequest,
                                           @AuthUser Member member) {
        updateTeamRequest.setCoverImageUrl(MediaUtil.removePrefix(updateTeamRequest.getCoverImageUrl()));
        Long updatedTeamId = teamCommandService.updateTeam(teamId, updateTeamRequest, member);
        return ApiResponseDto.onSuccess(updatedTeamId);
    }

    @Operation(summary = "팀 삭제 🔑", description = "팀을 삭제합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.TEAM_LEADER_UNAUTHORIZED,
            ErrorStatus.TEAM_MEMBER_IS_OVER_THAN_ONE
    }, status = AUTH)
    @DeleteMapping("/{teamId}")
    public ApiResponseDto<Boolean> deleteTeam(@PathVariable("teamId") Long teamId,
                                              @AuthUser Member member) {
        teamCommandService.deleteTeam(teamId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 강제 삭제 🔑", description = "팀이 관리자에 의해 삭제됩니다.")
    @ApiErrorCodeExample(status = ADMIN)
    @DeleteMapping("/{teamId}/admin")
    public ApiResponseDto<Boolean> deleteTeamByAdmin(@PathVariable("teamId") Long teamId,
                                                     @AuthUser Member admin) {
        teamCommandService.deleteTeamByAdmin(teamId, admin);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀원 삭제(수동) 🔑", description = "리더에 의해 지정된 팀에서 특정 팀원을 삭제합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.PARTICIPATION_NOT_FOUND,
            ErrorStatus.TEAM_LEADER_UNAUTHORIZED,
            ErrorStatus.TEAM_LEADER_CANNOT_BE_REMOVED
    }, status = AUTH)
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ApiResponseDto<Boolean> deleteTeamMemberByLeader(@PathVariable("teamId") Long teamId,
                                                            @PathVariable("memberId") Long memberId,
                                                            @AuthUser Member member) {
        teamCommandService.deleteTeamMemberByLeader(teamId, memberId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀원 삭제(능동) 🔑", description = "자신이 속한 팀으로부터 탈퇴합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.TEAM_LEADER_CANNOT_BE_REMOVED
    }, status = AUTH)
    @DeleteMapping("/{teamId}/members")
    public ApiResponseDto<Boolean> deleteTeamMemberByMyself(@PathVariable Long teamId,
                                                            @AuthUser Member member) {
        teamCommandService.deleteTeamMemberByMyself(teamId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 리더 변경 🔑", description = "지정된 팀의 리더를 변경합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.TEAM_LEADER_UNAUTHORIZED,
            ErrorStatus.TEAM_LEADER_CANNOT_BE_REMOVED,
            ErrorStatus.TEAM_MEMBER_NOT_IN_TEAM
    }, status = AUTH)
    @PutMapping("/{teamId}/leader/{newLeaderId}")
    public ApiResponseDto<Boolean> changeTeamLeader(@PathVariable("teamId") Long teamId,
                                                    @PathVariable("newLeaderId") Long newLeaderId,
                                                    @AuthUser Member member) {
        teamCommandService.changeTeamLeader(teamId, newLeaderId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 참여 요청 승인/거절 🔑", description = "팀 리더가 팀 참여 요청을 승인하거나 거절합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.TEAM_LEADER_UNAUTHORIZED,
            ErrorStatus.TEAM_PARTICIPATION_NOT_FOUND,
            ErrorStatus.TEAM_SIZE_IS_OVER_THAN_MAX_SIZE
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
            ErrorStatus.TEAM_NOT_FOUND
    })
    @GetMapping("/{teamId}")
    public ApiResponseDto<TeamDetailDto> getTeam(@PathVariable("teamId") Long teamId) {
        Team team = teamQueryService.getTeamById(teamId);
        return ApiResponseDto.onSuccess(TeamConverter.toDetailDto(team));
    }

    @Operation(summary = "팀 검색", description = "팀의 이름을 검색하여 팀 정보를 조회합니다.")
    @ApiErrorCodeExample
    @GetMapping("/by-name/{name}")
    public ApiResponseDto<TeamSummaryListDto> getTeamsBySearch(@PathVariable("name") String name,
                                                               @RequestParam(name = "currentPage", defaultValue = "0") int currentPage,
                                                               @RequestParam(name = "size", defaultValue = "0") int size) {
        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Team> teamByContainName = teamQueryService.getTeamByContainName(name, pageable);
        return ApiResponseDto.onSuccess(TeamConverter.toSummaryListDto(teamByContainName));
    }

    @Operation(summary = "추천 팀 조회", description = "가장 참여 인원이 많은 팀을 조회합니다.")
    @ApiErrorCodeExample
    @GetMapping("/recommend")
    public ApiResponseDto<TeamSummaryListDto> getRecommendedTeam(
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, TEAM_RECOMMEND_SIZE);
        Page<Team> teamByContainName = teamQueryService.getPopularTeamListTop3(pageable);
        return ApiResponseDto.onSuccess(TeamConverter.toSummaryListDto(teamByContainName));
    }

    @Operation(summary = "팀 참여 요청 목록 조회 🔑", description = "팀의 참여 요청 목록을 조회합니다. 팀의 리더 권한을 가진 회원만 조회할 수 있습니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.TEAM_LEADER_UNAUTHORIZED
    }, status = AUTH)
    @GetMapping("/{teamId}/participation")
    public ApiResponseDto<MemberSummaryListDto> getTeam(@AuthUser Member member,
                                                        @PathVariable("teamId") Long teamId) {
        List<Participation> participationList = teamQueryService.getParticipationList(member, teamId);
        List<Member> members = participationList.stream()
                .map(Participation::getMember)
                .collect(Collectors.toList());
        return ApiResponseDto.onSuccess(MemberConverter.toMemberListDto(members));
    }

    @Operation(summary = "팀 참여 상태 조회 🔑", description = "팀의 참여 요청 상태를 조회합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND
    }, status = AUTH)
    @GetMapping("/{teamId}/participation/status")
    public ApiResponseDto<ParticipationStatusResponse> getTeamParticipationStatus(@AuthUser Member member,
                                                                                  @PathVariable("teamId") Long teamId) {
        return ApiResponseDto.onSuccess(TeamConverter.toStatusDto(teamQueryService.getParticipationStatus(member, teamId)));
    }

    @Operation(summary = "사용자 팀 조회", description = "해당 사용자가 속한 팀 정보를 페이징하여 제공합니다.")
    @ApiErrorCodeExample
    @GetMapping("/user/{memberId}/teams")
    public ApiResponseDto<TeamSummaryListDto> getTeamsByMemberId(@PathVariable("memberId") Long memberId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Team> teams = teamQueryService.getPagedTeamByMemberId(memberId, pageable);
        return ApiResponseDto.onSuccess(TeamConverter.toSummaryListDto(teams));
    }

    @Operation(summary = "팀 참여 요청 🔑", description = "사용자가 팀에 참여 요청을 합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.TEAM_NOT_FOUND,
            ErrorStatus.TEAM_PARTICIPATION_REQUEST_ALREADY_EXISTS
    }, status = AUTH)
    @PostMapping("/{teamId}/participation")
    public ApiResponseDto<Long> requestParticipation(@PathVariable("teamId") Long teamId,
                                                     @AuthUser Member member) {
        return ApiResponseDto.onSuccess(teamCommandService.requestParticipation(teamId, member));
    }

}

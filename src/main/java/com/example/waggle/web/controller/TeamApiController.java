package com.example.waggle.web.controller;

import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.dto.schedule.TeamDto;
import com.example.waggle.web.dto.schedule.TeamRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@RestController
@Tag(name = "Team API", description = "팀 API")
public class TeamApiController {

    @Operation(summary = "팀 생성", description = "사용자가 팀을 생성합니다. 작성한 팀의 정보를 저장하고 팀의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "팀 생성 성공. 작성한 팀의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 팀 생성에 실패했습니다.")
    @PostMapping
    public ApiResponseDto<Long> createTeam(@RequestBody TeamRequest.TeamRequestDto request) {
        return ApiResponseDto.onSuccess(0L);
    }

    @Operation(summary = "팀 조회", description = "팀의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "팀 조회 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @GetMapping("/{teamId}")
    public ApiResponseDto<TeamDto> getTeam(@PathVariable Long teamId) {
        return ApiResponseDto.onSuccess(TeamDto.builder().build());
    }

    @Operation(summary = "팀 정보 업데이트", description = "팀의 정보를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "팀 정보 업데이트 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @PutMapping("/{teamId}")
    public ApiResponseDto<Long> updateTeam(@PathVariable Long teamId, @RequestBody TeamRequest.TeamRequestDto request) {
        return ApiResponseDto.onSuccess(0L);
    }

    @Operation(summary = "팀 삭제", description = "팀을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "팀 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @DeleteMapping("/{teamId}")
    public ApiResponseDto<Boolean> deleteTeam(@PathVariable Long teamId) {
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀원 추가", description = "지정된 팀에 새로운 팀원을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "팀원 추가 성공.")
    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없습니다.")
    @PostMapping("/{teamId}/members")
    public ApiResponseDto<Boolean> addTeamMember(@PathVariable Long teamId, @RequestBody TeamRequest.MemberDto request) {
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀원 삭제", description = "지정된 팀에서 특정 팀원을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "팀원 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "팀 또는 팀원을 찾을 수 없습니다.")
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ApiResponseDto<Boolean> deleteTeamMember(@PathVariable Long teamId, @PathVariable Long memberId) {
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "팀 리더 변경", description = "지정된 팀의 리더를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "팀 리더 변경 성공.")
    @ApiResponse(responseCode = "404", description = "팀 또는 멤버를 찾을 수 없습니다.")
    @PutMapping("/{teamId}/leader/{memberId}")
    public ApiResponseDto<Boolean> changeTeamLeader(@PathVariable Long teamId, @PathVariable Long memberId) {
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

}

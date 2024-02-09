package com.example.waggle.web.controller;

import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.service.schedule.ScheduleCommandService;
import com.example.waggle.domain.schedule.service.schedule.ScheduleQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.security.annotation.AuthUser;
import com.example.waggle.web.converter.ScheduleConverter;
import com.example.waggle.web.dto.schedule.ScheduleRequest.Post;
import com.example.waggle.web.dto.schedule.ScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
@Tag(name = "Schedule API", description = "일정 API")
public class ScheduleApiController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;
    private Sort latestStart = Sort.by("startTime").descending();

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "일정 생성 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 작성에 실패했습니다.")
    @PostMapping("/{teamId}")
    public ApiResponseDto<Long> createSchedule(@AuthUser UserDetails userDetails,
                                               @PathVariable Long teamId,
                                               @RequestBody Post request) {
        Long createdScheduleId = scheduleCommandService.createSchedule(teamId, request, userDetails.getUsername());
        return ApiResponseDto.onSuccess(createdScheduleId);
    }

    @Operation(summary = "일정 추가", description = "사용자의 팀내 일정을 선택하여 추가합니다.")
    @ApiResponse(responseCode = "200", description = "일정 추가 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 저장에 실패했습니다.")
    @PostMapping("/members/{scheduleId}")
    public ApiResponseDto<Long> addSchedule(@AuthUser UserDetails userDetails,
                                            @PathVariable Long scheduleId) {
        Long createdScheduleId = scheduleCommandService.addMemberSchedule(scheduleId, userDetails.getUsername());
        return ApiResponseDto.onSuccess(createdScheduleId);
    }

    @Operation(summary = "특정 일정 조회", description = "특정 일정의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/{scheduleId}")
    public ApiResponseDto<ScheduleResponse.DetailDto> getSchedule(@PathVariable Long scheduleId) {
        Schedule schedule = scheduleQueryService.getScheduleById(scheduleId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleResponseDto(schedule));
    }

    @Operation(summary = "일정 수정", description = "특정 일정의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "일정 수정 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 수정에 실패했습니다.")
    @PutMapping("/{scheduleId}")
    public ApiResponseDto<Long> updateSchedule(@PathVariable Long scheduleId,
                                               @RequestBody Post request) {
        Long updatedScheduleId = scheduleCommandService.updateSchedule(scheduleId, request);
        return ApiResponseDto.onSuccess(updatedScheduleId);
    }

    @Operation(summary = "일정 삭제", description = "특정 일정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "일정 삭제 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 삭제에 실패했습니다.")
    @DeleteMapping("/teams")
    public ApiResponseDto<Boolean> deleteScheduleInTeam(@RequestParam("boardId") Long boardId) {
        scheduleCommandService.deleteSchedule(boardId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "일정 삭제", description = "특정 일정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "일정 삭제 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 삭제에 실패했습니다.")
    @DeleteMapping("/members/{boardId}")
    public ApiResponseDto<Boolean> deleteScheduleInMember(@AuthUser UserDetails userDetails,
                                                          @RequestParam("boardId") Long boardId) {
        scheduleCommandService.deleteMemberSchedule(boardId, userDetails.getUsername());
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "특정 팀의 모든 일정 조회", description = "특정 팀의 모든 일정을 가져옵니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/teams/{teamId}")
    public ApiResponseDto<ScheduleResponse.ListDto> getSchedulesByTeam(@PathVariable Long teamId) {
        List<Schedule> schedules = scheduleQueryService.getTeamSchedules(teamId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

    @Operation(summary = "특정 팀의 모든 일정 조회", description = "특정 팀의 모든 일정을 가져옵니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/teams/{teamId}/page")
    public ApiResponseDto<ScheduleResponse.ListDto> getPagedSchedulesByTeam(@RequestParam(defaultValue = "0") int currentPage,
                                                                            @PathVariable Long teamId) {
        Pageable pageable = PageRequest.of(currentPage, 12, latestStart);
        Page<Schedule> pagedSchedules = scheduleQueryService.getPagedTeamSchedules(teamId, pageable);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(pagedSchedules));
    }

    @Operation(summary = "특정 사용자의 모든 일정 조회", description = "특정 사용자가 선택한 모든 일정을 가져옵니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/members/{memberId}")
    public ApiResponseDto<ScheduleResponse.ListDto> getSchedulesByMember(@PathVariable Long memberId) {
        List<Schedule> schedules = scheduleQueryService.getSchedulesByMember(memberId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

    @Operation(summary = "특정 사용자가 작성한 일정 조회", description = "특정 사용자가 작성한 모든 일정을 가져옵니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/writers/{memberId}")
    public ApiResponseDto<ScheduleResponse.ListDto> getSchedulesByWriter(@PathVariable Long memberId) {
        List<Schedule> schedules = scheduleQueryService.getSchedulesByWriter(memberId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

    @Operation(summary = "특정 팀의 월 일정 조회", description = "특정 팀의 스케줄 전체를 월 단위로 가져옵니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/teams/{teamId}/monthly")
    public ApiResponseDto<ScheduleResponse.ListDto> getMonthlySchedulesForTeam(
            @PathVariable Long teamId,
            @RequestParam int year,
            @RequestParam int month) {

        List<Schedule> schedules = scheduleQueryService.getMonthlyTeamSchedule(teamId, year, month);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

    @Operation(summary = "특정 사용자의 월 일정 조회", description = "특정 사용자가 선택한 팀 스케줄 전체를 월단위로 가져옵니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/members/{memberId}/monthly")
    public ApiResponseDto<ScheduleResponse.ListDto> getMonthlySchedulesForMember(
            @PathVariable Long memberId,
            @RequestParam int year,
            @RequestParam int month) {

        List<Schedule> schedules = scheduleQueryService.getMonthlySchedulesByMember(memberId, year, month);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

    @Operation(summary = "기간 해당 팀 일정 조회", description = "사용자가 검색한 기간에 해당하는 팀 스케줄을 모두 가져옵니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/teams/{teamId}/period")
    public ApiResponseDto<ScheduleResponse.ListDto> getTeamScheduleByPeriod(
            @PathVariable Long teamId,
            @RequestParam(value = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Schedule> schedules = scheduleQueryService.getTeamScheduleByPeriod(teamId, start, end);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

}

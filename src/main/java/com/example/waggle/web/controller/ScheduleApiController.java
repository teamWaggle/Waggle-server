package com.example.waggle.web.controller;

import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.service.ScheduleCommandService;
import com.example.waggle.domain.schedule.service.ScheduleQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.converter.ScheduleConverter;
import com.example.waggle.web.dto.schedule.ScheduleRequest.Post;
import com.example.waggle.web.dto.schedule.ScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
@Tag(name = "Schedule API", description = "일정 API")
public class ScheduleApiController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "일정 생성 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 작성에 실패했습니다.")
    @PostMapping("/{teamId}")
    public ApiResponseDto<Long> createSchedule(@PathVariable Long teamId,
                                               @RequestBody Post request) {
        Long createdScheduleId = scheduleCommandService.createSchedule(teamId, request, SecurityUtil.getCurrentUsername());
        return ApiResponseDto.onSuccess(createdScheduleId);
    }

    @Operation(summary = "일정 조회", description = "특정 일정의 정보를 조회합니다.")
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
    @DeleteMapping
    public ApiResponseDto<Boolean> deleteSchedule(@RequestParam("boardId") Long boardId) {
        scheduleCommandService.deleteSchedule(boardId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "특정 사용자가 속한 팀의 모든 일정 조회", description = "특정 사용자가 속한 모든 팀의 일정을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/members/{memberId}")
    public ApiResponseDto<ScheduleResponse.ListDto> getSchedulesByMemberId(@PathVariable Long memberId) {
        List<Schedule> schedules = scheduleQueryService.getSchedulesByMemberId(memberId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

    @Operation(summary = "특정 사용자가 속한 팀의 특정 월 일정 조회", description = "특정 사용자가 속한 모든 팀의 특정 월에 대한 일정을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 스케줄 조회에 실패했습니다.")
    @GetMapping("/members/{memberId}/monthly")
    public ApiResponseDto<ScheduleResponse.ListDto> getMonthlySchedulesForMember(
            @PathVariable Long memberId,
            @RequestParam int year,
            @RequestParam int month) {

        List<Schedule> schedules = scheduleQueryService.getMonthlySchedulesByMemberId(memberId, year, month);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }

}

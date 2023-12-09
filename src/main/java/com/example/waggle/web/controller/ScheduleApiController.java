package com.example.waggle.web.controller;

import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.service.ScheduleCommandService;
import com.example.waggle.domain.schedule.service.ScheduleQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.ScheduleConverter;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import com.example.waggle.web.dto.schedule.ScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@RequestMapping("/api/schedules")
@RestController
@Tag(name = "Schedule API", description = "일정 API")
public class ScheduleApiController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "일정 생성 성공.")
    @PostMapping("/{teamId}")
    public ApiResponseDto<Long> createSchedule(@PathVariable Long teamId,
                                               @RequestBody ScheduleRequest.ScheduleRequestDto request) {
        Long createdScheduleId = scheduleCommandService.createSchedule(teamId, request);
        return ApiResponseDto.onSuccess(createdScheduleId);
    }

    @Operation(summary = "일정 조회", description = "특정 일정의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @GetMapping("/{scheduleId}")
    public ApiResponseDto<ScheduleResponse.ScheduleResponseDto> getSchedule(@PathVariable Long scheduleId) {
        Schedule schedule = scheduleQueryService.getScheduleById(scheduleId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleResponseDto(schedule));
    }

    @Operation(summary = "일정 수정", description = "특정 일정의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "일정 수정 성공.")
    @PutMapping("/{scheduleId}")
    public ApiResponseDto<Long> updateSchedule(@PathVariable Long scheduleId,
                                               @RequestBody ScheduleRequest.ScheduleRequestDto request) {
        Long updatedScheduleId = scheduleCommandService.updateSchedule(scheduleId, request);
        return ApiResponseDto.onSuccess(updatedScheduleId);
    }

    @Operation(summary = "일정 삭제", description = "특정 일정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "일정 삭제 성공.")
    @DeleteMapping("/{scheduleId}")
    public ApiResponseDto<Boolean> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleCommandService.deleteSchedule(scheduleId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "특정 사용자가 속한 팀의 모든 일정 조회", description = "특정 사용자가 속한 모든 팀의 일정을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일정 조회 성공.")
    @GetMapping("/members/{username}")
    public ApiResponseDto<ScheduleResponse.ListDto> getSchedulesByMemberUsername(@PathVariable String username) {
        List<Schedule> schedules = scheduleQueryService.getSchedulesByMemberUsername(username);
        return ApiResponseDto.onSuccess(ScheduleConverter.toListDto(schedules));
    }


}

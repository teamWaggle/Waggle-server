package com.example.waggle.web.controller;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.service.schedule.ScheduleCommandService;
import com.example.waggle.domain.schedule.service.schedule.ScheduleQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.ScheduleUtil;
import com.example.waggle.web.converter.MemberConverter;
import com.example.waggle.web.converter.ScheduleConverter;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryListDto;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import com.example.waggle.web.dto.schedule.ScheduleResponse.OverlappedScheduleDto;
import com.example.waggle.web.dto.schedule.ScheduleResponse.ScheduleDetailDto;
import com.example.waggle.web.dto.schedule.ScheduleResponse.ScheduleListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Schedule API", description = "일정 API")
public class ScheduleApiController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;
    private Sort latestStart = Sort.by("startTime").descending();

    @Operation(summary = "팀 일정 작성 🔑", description = "새로운 일정을 생성합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/{teamId}")
    public ApiResponseDto<Long> createSchedule(@PathVariable("teamId") Long teamId,
                                               @RequestBody @Validated ScheduleRequest createScheduleRequest,
                                               @AuthUser Member member) {
        Long createdScheduleId = scheduleCommandService.createSchedule(teamId, createScheduleRequest, member);
        return ApiResponseDto.onSuccess(createdScheduleId);
    }

    @Operation(summary = "개인 일정 선택 🔑", description = "사용자의 팀내 일정을 선택하여 추가합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/members/{scheduleId}")
    public ApiResponseDto<Long> addSchedule(@PathVariable("scheduleId") Long scheduleId,
                                            @AuthUser Member member) {
        Long createdScheduleId = scheduleCommandService.addMemberSchedule(scheduleId, member);
        return ApiResponseDto.onSuccess(createdScheduleId);
    }

    @Operation(summary = "특정 일정 조회", description = "특정 일정의 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{scheduleId}")
    public ApiResponseDto<ScheduleDetailDto> getSchedule(@PathVariable("scheduleId") Long scheduleId) {
        Schedule schedule = scheduleQueryService.getScheduleById(scheduleId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleDetailDto(schedule));
    }

    @Operation(summary = "일정 삭제 🔑", description = "특정 일정을 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{scheduleId}")
    public ApiResponseDto<Boolean> deleteScheduleInTeam(@PathVariable("scheduleId") Long scheduleId,
                                                        @AuthUser Member member) {
        scheduleCommandService.deleteSchedule(scheduleId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "일정 수정 🔑", description = "특정 일정의 정보를 수정합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/{scheduleId}")
    public ApiResponseDto<Long> updateSchedule(@PathVariable("scheduleId") Long scheduleId,
                                               @RequestBody ScheduleRequest updateScheduleRequest,
                                               @AuthUser Member member) {
        Long updatedScheduleId = scheduleCommandService.updateSchedule(scheduleId, updateScheduleRequest, member);
        return ApiResponseDto.onSuccess(updatedScheduleId);
    }

    @Operation(summary = "일정 취소 🔑", description = "특정 일정을 취소합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{scheduleId}/members")
    public ApiResponseDto<Boolean> deleteScheduleInMember(@PathVariable("scheduleId") Long scheduleId,
                                                          @AuthUser Member member) {
        scheduleCommandService.deleteMemberSchedule(scheduleId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "특정 팀의 모든 일정 조회", description = "특정 팀의 모든 일정을 가져옵니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/teams/{teamId}")
    public ApiResponseDto<ScheduleListDto> getSchedulesByTeam(@PathVariable("teamId") Long teamId) {
        List<Schedule> schedules = scheduleQueryService.getTeamSchedules(teamId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleListDto(schedules));
    }

    @Operation(summary = "특정 팀의 모든 일정 조회 🔑", description = "특정 팀의 모든 일정을 가져옵니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/teams/{teamId}/page")
    public ApiResponseDto<ScheduleListDto> getPagedSchedulesByTeam(@AuthUser Member member,
                                                                   @PathVariable("teamId") Long teamId,
                                                                   @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 12, latestStart);
        Page<Schedule> pagedSchedules = scheduleQueryService.getPagedTeamSchedules(member, teamId, pageable);
        ScheduleListDto scheduleListDto = ScheduleConverter.toScheduleListDto(pagedSchedules);
        setIsScheduledInList(member, scheduleListDto);
        setOverlappedScheduleInList(member, scheduleListDto);
        return ApiResponseDto.onSuccess(scheduleListDto);
    }

    @Operation(summary = "특정 사용자의 모든 일정 조회", description = "특정 사용자가 선택한 모든 일정을 가져옵니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/members/{memberId}")
    public ApiResponseDto<ScheduleListDto> getSchedulesByMember(@PathVariable("memberId") Long memberId) {
        List<Schedule> schedules = scheduleQueryService.getSchedulesByMember(memberId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleListDto(schedules));
    }

    @Operation(summary = "특정 사용자가 작성한 일정 조회", description = "특정 사용자가 작성한 모든 일정을 가져옵니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/writers/{memberId}")
    public ApiResponseDto<ScheduleListDto> getSchedulesByWriter(@PathVariable("memberId") Long memberId) {
        List<Schedule> schedules = scheduleQueryService.getSchedulesByWriter(memberId);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleListDto(schedules));
    }

    @Operation(summary = "특정 팀의 월간 일정 조회", description = "특정 팀의 스케줄 전체를 월 단위로 가져옵니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/teams/{teamId}/monthly")
    public ApiResponseDto<ScheduleListDto> getMonthlySchedulesForTeam(@PathVariable("teamId") Long teamId,
                                                                      @RequestParam("year") int year,
                                                                      @RequestParam("month") int month) {
        List<Schedule> schedules = scheduleQueryService.getMonthlyTeamSchedule(teamId, year, month);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleListDto(schedules));
    }

    @Operation(summary = "특정 사용자의 월간 일정 조회", description = "특정 사용자가 선택한 팀 스케줄 전체를 월단위로 가져옵니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/members/{userUrl}/monthly")
    public ApiResponseDto<ScheduleListDto> getMonthlySchedulesForMember(@PathVariable("userUrl") String userUrl,
                                                                        @RequestParam("year") int year,
                                                                        @RequestParam("month") int month) {
        List<Schedule> schedules = scheduleQueryService.getMonthlySchedulesByMemberUserUrl(userUrl, year, month);
        return ApiResponseDto.onSuccess(ScheduleConverter.toScheduleListDto(schedules));
    }

    @Operation(summary = "기간 해당 팀 일정 조회 🔑", description = "사용자가 검색한 기간에 해당하는 팀 스케줄을 모두 가져옵니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/teams/{teamId}/period")
    public ApiResponseDto<ScheduleListDto> getTeamScheduleByPeriod(@AuthUser Member member,
                                                                   @PathVariable("teamId") Long teamId,
                                                                   @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                                   @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        ScheduleUtil.validateSchedule(start, end);
        List<Schedule> schedules = scheduleQueryService.getTeamScheduleByPeriod(member, teamId, start, end);
        ScheduleListDto scheduleListDto = ScheduleConverter.toScheduleListDto(schedules);
        setIsScheduledInList(member, scheduleListDto);
        setOverlappedScheduleInList(member, scheduleListDto);
        return ApiResponseDto.onSuccess(scheduleListDto);
    }

    @Operation(summary = "스케줄 선택 멤버 조회", description = "특정한 팀 스케줄을 선택한 멤버들을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{scheduleId}/members")
    public ApiResponseDto<MemberSummaryListDto> getMembersBySchedules(@PathVariable("scheduleId") Long
                                                                              scheduleId) {
        List<Member> memberBySchedule = scheduleQueryService.getMemberBySchedule(scheduleId);
        return ApiResponseDto.onSuccess(MemberConverter.toMemberListDto(memberBySchedule));
    }

    private void setIsScheduledInList(Member member, ScheduleListDto scheduleListDto) {
        scheduleListDto.getScheduleList()
                .forEach(schedule ->
                        schedule.setIsScheduled(
                                scheduleQueryService.getIsScheduled(member, schedule.getBoardId())));
    }

    private void setOverlappedScheduleInList(Member member, ScheduleListDto scheduleListDto) {
        scheduleListDto.getScheduleList().forEach(scheduleDto -> {
            List<Schedule> overlappingSchedules = scheduleQueryService.findOverlappingSchedules(member,
                    scheduleDto.getBoardId());
            List<OverlappedScheduleDto> overlappedScheduleDtos = overlappingSchedules.stream()
                    .map(ScheduleConverter::toOverlappedScheduleDto)
                    .collect(Collectors.toList());
            scheduleDto.setOverlappedScheduleList(overlappedScheduleDtos);
            scheduleDto.setOverlappedScheduleCount((int) overlappedScheduleDtos.stream().count());
        });
    }


}

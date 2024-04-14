//package com.example.waggle.web.controller;
//
//import com.example.waggle.domain.member.entity.Member;
//import com.example.waggle.domain.notification.entity.alarm.Alarm;
//import com.example.waggle.global.annotation.ApiErrorCodeExample;
//import com.example.waggle.global.annotation.auth.AuthUser;
//import com.example.waggle.global.payload.ApiResponseDto;
//import com.example.waggle.global.payload.code.ErrorStatus;
//import com.example.waggle.global.util.TimeUtil;
//import com.example.waggle.web.converter.AlarmConverter;
//import com.example.waggle.web.dto.alarm.AlarmResponse.AlarmListDto;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RequestMapping("/api/alarms")
//@RequiredArgsConstructor
//@RestController
//@ApiResponse(responseCode = "2000", description = "성공")
//@Tag(name = "Alarm API", description = "알림 API")
//public class AlarmApiController {
//    private final AlarmService alarmService;
//
//    @Operation(summary = "알림 목록 조회 🔑", description = "사용자에게 발송된 알림들을 확인합니다.")
//    @ApiErrorCodeExample({
//            ErrorStatus._INTERNAL_SERVER_ERROR
//    })
//    @GetMapping
//    public ApiResponseDto<AlarmListDto> getAlarms(@AuthUser Member member) {
//        List<Alarm> alarms = alarmService.sendAlarmSliceAndIsReadToTrue(Pageable.unpaged(), member);
//        return ApiResponseDto.onSuccess(AlarmConverter.toAlarmListDto(alarms));
//    }
//
//    @Operation(summary = "알림 sse 구독 🔑", description = "사용자가 sse 구독하여 실시간 알람을 받습니다.")
//    @ApiErrorCodeExample({
//            ErrorStatus._INTERNAL_SERVER_ERROR
//    })
//    @GetMapping(value = "/subscribe", produces = "text/event-stream")
//    public SseEmitter subscribeAlarm(@AuthUser Member member,
//                                     HttpServletResponse response) {
//        LocalDateTime current = TimeUtil.nowWithoutNano();
//        response.setHeader("X-Accel-Buffering", "no");
//        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
//        return alarmService.subscribe(member, current);
//    }
//}

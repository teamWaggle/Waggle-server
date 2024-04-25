package com.example.waggle.web.controller;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.domain.notification.service.NotificationCommandService;
import com.example.waggle.domain.notification.service.NotificationQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.NotificationConverter;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationCountDto;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Notification API", description = "알림 API")
public class NotificationApiController {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;
    Sort latestSorting = Sort.by("createdDate").descending();
    Sort readSorting = Sort.by("isRead").ascending().and(latestSorting);

    @Operation(summary = "알림 목록 조회 🔑", description = "사용자에게 발송된 알림목록을 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping
    public ApiResponseDto<NotificationListDto> getNotifications(@AuthUser Member member,
                                                                @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 7, readSorting);
        Page<Notification> notificationList = notificationQueryService.getNotificationList(member, pageable);
        return ApiResponseDto.onSuccess(NotificationConverter.toListDto(notificationList));
    }

    @Operation(summary = "미열람 알림 개수 조회 🔑", description = "발송된 알림 중 미열람 알림의 개수를 사용자에게 전송합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping(value = "/count")
    public ApiResponseDto<NotificationCountDto> getNotifications(@AuthUser Member member) {
        return ApiResponseDto.onSuccess(NotificationConverter
                .toCountDto(notificationQueryService.countNotReadNotification(member)));
    }

    @Operation(summary = "미열람 상태 전환 🔑", description = "사용자가 알림을 상세조회 시에 미열람 상태를 열람으로 바꾸어줍니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(value = "/status/{notificationId}")
    public ApiResponseDto<Boolean> getNotifications(@AuthUser Member member,
                                                    @PathVariable Long notificationId) {
        notificationCommandService.markNotificationAsRead(member, notificationId);
        return ApiResponseDto.onSuccess(true);
    }

}

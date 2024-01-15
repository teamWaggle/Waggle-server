package com.example.waggle.web.controller;

import com.example.waggle.domain.chat.service.RoomService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.dto.chat.RoomRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
@RestController
@Tag(name = "Room API", description = "채팅방 API")
public class RoomApiController {

    private final RoomService roomService;


    @PostMapping
    public ApiResponseDto<String> createRoom(@RequestBody RoomRequest.Post request) {
        return ApiResponseDto.onSuccess(roomService.createChatRoom(request));
    }


}

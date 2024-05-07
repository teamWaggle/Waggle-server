package com.example.waggle.domain.chat.presentation.dto;

import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ChatRoomResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ChatRoomDetailDto {
        private Long id;
        private String name;
        private String description;
        private MemberSummaryListDto chatRoomMembers;
        private MemberSummaryDto owner;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ChatRoomListDto {
        private List<ChatRoomDetailDto> chatRooms;
    }
}

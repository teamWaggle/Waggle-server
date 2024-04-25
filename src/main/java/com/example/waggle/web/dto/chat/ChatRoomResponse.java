package com.example.waggle.web.dto.chat;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

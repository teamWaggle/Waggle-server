package com.example.waggle.domain.chat.presentation.controller;

import com.example.waggle.domain.chat.application.chatMessage.ChatMessageQueryService;
import com.example.waggle.domain.chat.application.chatRoom.ChatRoomCommandService;
import com.example.waggle.domain.chat.application.chatRoom.ChatRoomQueryService;
import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import com.example.waggle.domain.chat.presentation.converter.ChatConverter;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse;
import com.example.waggle.domain.chat.presentation.dto.ChatRoomRequest;
import com.example.waggle.domain.member.application.MemberQueryService;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Chat API", description = "채팅 API")
public class ChatApiController {

    private final ChatRoomCommandService chatRoomCommandService;
    private final ChatRoomQueryService chatRoomQueryService;
    private final ChatMessageQueryService chatMessageQueryService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "채팅방 생성 🔑", description = "사용자가 새로운 채팅방을 생성합니다. 생성 성공 시 채팅방의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/rooms")
    public ApiResponseDto<Long> createChatRoom(@AuthUser Member member, @RequestBody ChatRoomRequest request) {
        return ApiResponseDto.onSuccess(chatRoomCommandService.createChatRoom(member, request));
    }

    @Operation(summary = "채팅방 입장 🔑", description = "사용자가 지정한 채팅방에 입장합니다. 입장 성공 시 입장한 채팅방의 멤버 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.CHAT_ROOM_NOT_FOUND
    })
    @PostMapping("/rooms/{chatRoomId}/join")
    public ApiResponseDto<Long> joinChatRoom(@AuthUser Member member, @PathVariable("chatRoomId") Long chatRoomId,
                                             @RequestParam(value = "password", required = false) String password) {
        LocalDateTime now = LocalDateTime.now();
        chatRoomCommandService.joinChatRoom(member, chatRoomId, password);
        Long updatedChatRoomMemberId = chatRoomCommandService.updateLastAccessTime(member, chatRoomId, now);
        return ApiResponseDto.onSuccess(updatedChatRoomMemberId);
    }

    @Operation(summary = "채팅방 수정 🔑", description = "채팅방 호스트가 채팅방을 수정합니다. 호스트만 채팅방 수정 권한을 가지며, 성공적으로 수정 시 채팅방의 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.CHAT_ROOM_NOT_FOUND
    })
    @PutMapping("/rooms/{chatRoomId}")
    public ApiResponseDto<Long> updateChatRoom(@AuthUser Member member, @PathVariable("chatRoomId") Long chatRoomId,
                                               @RequestBody ChatRoomRequest request) {
        return ApiResponseDto.onSuccess(chatRoomCommandService.updateChatRoom(member, chatRoomId, request));
    }


    @Operation(summary = "채팅방 삭제 🔑", description = "채팅방 호스트가 채팅방을 삭제합니다. 호스트만 채팅방 삭제 권한을 가지며, 성공적으로 삭제 시 true를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.CHAT_ROOM_NOT_FOUND,
            ErrorStatus.CHAT_ROOM_ACCESS_DENIED,
            ErrorStatus.CHAT_ROOM_LEAVE_DENIED
    })
    @DeleteMapping("/rooms/{chatRoomId}")
    public ApiResponseDto<Boolean> deleteChatRoom(@AuthUser Member member,
                                                  @PathVariable("chatRoomId") Long chatRoomId) {
        chatRoomCommandService.deleteChatRoom(member, chatRoomId);
        return ApiResponseDto.onSuccess(true);
    }

    @Operation(summary = "채팅방 퇴장 🔑", description = "사용자가 채팅방에서 퇴장합니다. 사용자가 채팅방 멤버가 아닐 경우 에러가 발생하며, 성공적으로 삭제 시 true를 반환합니다. 채팅방 소유자는 채팅방에 다른 회원이 존재하면 퇴장할 수 없습니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.CHAT_ROOM_NOT_FOUND,
            ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND
    })
    @DeleteMapping("/rooms/{chatRoomId}/leave")
    public ApiResponseDto<Boolean> leaveChatRoom(@AuthUser Member member, @PathVariable("chatRoomId") Long chatRoomId) {
        chatRoomCommandService.leaveChatRoom(member, chatRoomId);
        return ApiResponseDto.onSuccess(true);
    }

    @Operation(summary = "채팅방 목록 전체 조회 (페이징 X)", description = "모든 채팅방의 목록을 조회합니다. (페이징 X, 테스트용)")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/rooms/all")
    public ApiResponseDto<ChatResponse.ChatRoomListDto> getChatRooms() {
        return ApiResponseDto.onSuccess(ChatConverter.toChatRoomListDto(chatRoomQueryService.getChatRooms()));
    }

    @Operation(summary = "채팅방 목록 전체 조회 (페이징 O)", description = "채팅방 목록을 조회합니다. 페이지의 크기는 9입니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/rooms/paged")
    public ApiResponseDto<ChatResponse.ChatRoomListDto> getPagedChatRooms(
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.CHAT_ROOM_SIZE);
        return ApiResponseDto.onSuccess(
                ChatConverter.toChatRoomListDto(chatRoomQueryService.getPagedChatRooms(pageable).getContent()));
    }

    @Operation(summary = "특정 채팅방 조회", description = "특정 채팅방을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.CHAT_ROOM_NOT_FOUND
    })
    @GetMapping("/rooms/{chatRoomId}")
    public ApiResponseDto<ChatResponse.ChatRoomDetailDto> getChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        return ApiResponseDto.onSuccess(
                ChatConverter.toChatRoomDetailDto(chatRoomQueryService.getChatRoomById(chatRoomId)));
    }

    @Operation(summary = "특정 회원이 참여중인 채팅방 목록 전체 조회 (페이징 O)", description = "회원이 참여중인 채팅방 목록을 조회합니다. 페이지의 크기는 9입니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/rooms/active")
    public ApiResponseDto<ChatResponse.ActiveChatRoomListDto> getPagedChatRoomsByMember(@AuthUser Member member,
                                                                                        @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.CHAT_ROOM_SIZE);
        Page<ChatRoom> chatRooms = chatRoomQueryService.getPagedActiveChatRoomsByMember(member, pageable);
        List<ChatResponse.ActiveChatRoomDto> activeChatRooms = chatRooms.getContent().stream()
                .map(room -> buildActiveChatRoomDto(member, room))
                .collect(Collectors.toList());
        return ApiResponseDto.onSuccess(ChatConverter.toActiveChatRoomList(activeChatRooms));
    }

    private ChatResponse.ActiveChatRoomDto buildActiveChatRoomDto(Member member, ChatRoom room) {
        long unreadCount = chatRoomQueryService.getUnreadMessagesCount(member, room.getId());
        String lastMessageContent = chatRoomQueryService.getLastMessageContent(room.getId());
        return ChatConverter.toActiveChatRoomDto(room, unreadCount, lastMessageContent);
    }

    @Operation(summary = "특정 채팅 내역 목록 조회 (페이징 O)", description = "특정 채팅 내역 목록을 조회합니다. 페이지의 크기는 20입니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/rooms/{chatRoomId}/messages")
    public ApiResponseDto<ChatResponse.ChatMessageListDto> getPagedChatMessages(@AuthUser Member member,
                                                                                @PathVariable("chatRoomId") Long chatRoomId,
                                                                                @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.CHAT_MESSAGE_SIZE);
        Page<ChatMessage> chatMessages = chatMessageQueryService.getPagedChatMessages(member, chatRoomId,
                pageable);
        List<ChatResponse.ChatMessageDto> chatMessageList = chatMessages.getContent().stream()
                .map(this::buildChatMessageDto)
                .collect(Collectors.toList());
        return ApiResponseDto.onSuccess(ChatConverter.toChatMessageListDto(chatMessageList));
    }

    private ChatResponse.ChatMessageDto buildChatMessageDto(ChatMessage chatMessage) {
        Member sender = memberQueryService.getMemberByUserUrl(chatMessage.getSenderUserUrl());
        return ChatConverter.toChatMessageDto(chatMessage, sender);
    }

}

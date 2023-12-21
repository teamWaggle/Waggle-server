package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.domain.Room;
import com.example.waggle.domain.chat.domain.RoomMember;
import com.example.waggle.domain.chat.domain.RoomMemberRepository;
import com.example.waggle.domain.chat.domain.RoomRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberQueryService memberQueryService;

    @Transactional
    public String createChatRoom(String chatRoomName, String username) {
        Member host = memberQueryService.getMemberByUsername(username);

        Room createdRoom = Room.builder()
                .id(Room.createRoomId())
                .name(chatRoomName)
                .host(host)
                .build();

        addMemberToRoom(createdRoom, host);

        return roomRepository.save(createdRoom).getId();
    }

    public void connectChatRoom(String chatRoomId, String username) {
        Room room = roomRepository.findById(chatRoomId).get();    // TODO 예외 처리
        Member member = memberQueryService.getMemberByUsername(username);
        addMemberToRoom(room, member);
    }

    private void addMemberToRoom(Room room, Member member) {
        RoomMember roomMember = RoomMember.builder()
                .room(room)
                .member(member).build();
        roomMember.addRoomMember(room, member);
        roomMemberRepository.save(roomMember);
    }

}

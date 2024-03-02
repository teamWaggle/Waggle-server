package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.Room;
import com.example.waggle.domain.chat.entity.RoomMember;
import com.example.waggle.domain.chat.repository.RoomRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.web.dto.chat.RoomRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;

    @Transactional
    public String createChatRoom(RoomRequest.Post request) {
        Member host = memberQueryService.getSignInMember();
        memberRepository.save(host);

        Room createdRoom = Room.builder()
                .id(Room.createRoomId())
                .name(request.getName())
                .host(host)
                .build();
        Room room = roomRepository.save(createdRoom);
        addMemberToRoom(room, host);

//        String topicName = "chat-room-" + room.getId(); // 고유한 토픽 이름 생성
//        NewTopic newTopic = new NewTopic(topicName, 1, (short) 1); // 파티션 1개, 복제 인자 1로 새 토픽 생성
//        kafkaAdmin.createOrModifyTopics(newTopic);

        return room.getId();
    }

    private void addMemberToRoom(Room room, Member member) {
        RoomMember roomMember = RoomMember.builder()
                .room(room)
                .member(member).build();
        room.getRoomMembers().add(roomMember);
    }

    public void connectChatRoom(String chatRoomId, String username) {
        Room room = roomRepository.findById(chatRoomId).get();    // TODO 예외 처리
        Member member = memberQueryService.getMemberByUsername(username);
        memberRepository.save(member);
        addMemberToRoom(room, member);
    }

    public Set<Member> getMembersExceptSender(String chatRoomId, Long senderId) {
        Room room = roomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        Set<RoomMember> roomMembers = room.getRoomMembers();

        return roomMembers.stream()
                .map(RoomMember::getMember)
                .filter(member -> !member.getId().equals(senderId))
                .collect(Collectors.toSet());
    }

    public List<Room> getRoomList() {
        return roomRepository.findAll();
    }

    public Room getRoom(String roomId) {
        return roomRepository.findById(roomId).get();
    }
}

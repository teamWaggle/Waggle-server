package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.dto.Message;
import com.example.waggle.domain.chat.entity.Chat;
import com.example.waggle.domain.chat.repository.MongoChatRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatService {

    private final MemberQueryService memberQueryService;
    private final MongoChatRepository mongoChatRepository;
    private final KafkaTemplate kafkaTemplate;
    private final RoomService roomService;
    private final MessageSender sender;

    public void sendMessage(Message message, String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        Integer readCount = 0; // TODO 메시지 안 읽은 사람 수
        message.setSendTimeAndSender(member.getUsername(), member.getId(), LocalDateTime.now(), readCount);
        sender.send("waggle-chat", message);
    }

    @Transactional
    public Message sendNotificationAndSaveMessage(Message message) {
        Member sendMember = memberQueryService.getMemberByUsername(message.getSenderUsername());

        // TODO 채팅방의 전체 멤버들에게 알림을 보내되, 메시지를 보낸 사람은 제외한다.
        /*
        if (message.getReadCount().equals(1)) {
            Set<Member> members = roomService.getMembersExceptSender(message.getChatRoomId(), message.getSenderId());
            for (Member receiveMember : members) {
                String content = message.getContentType().equals("image") ? "image" : message.getContent();
                String sendUrl = getNotificationUrl(message.getSaleNo(), message.getChatNo());
                notificationService.send(sendMember, receiveMember, NotifiTypeEnum.CHAT, sendUrl, content);
            }
        }
        */

        // 메시지 저장 로직
        Chat chatting = message.toEntity();
        Chat savedChat = mongoChatRepository.save(chatting);
        message.setId(savedChat.getId());

        return message;
    }


}

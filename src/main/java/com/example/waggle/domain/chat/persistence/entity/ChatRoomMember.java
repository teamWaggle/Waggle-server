package com.example.waggle.domain.chat.persistence.entity;

import com.example.waggle.domain.auditing.persistence.entity.BaseEntity;
import com.example.waggle.domain.member.persistence.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private LocalDateTime lastAccessTime;

    @Builder
    public ChatRoomMember(Member member, ChatRoom chatRoom, LocalDateTime lastAccessTime) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.lastAccessTime = lastAccessTime;
    }

    public void updateLastAccessTime(LocalDateTime time) {
        this.lastAccessTime = time;
    }
}

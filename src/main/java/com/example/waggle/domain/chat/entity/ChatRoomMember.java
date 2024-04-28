package com.example.waggle.domain.chat.entity;

import com.example.waggle.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoomMember {

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

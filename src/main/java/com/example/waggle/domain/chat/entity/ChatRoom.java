package com.example.waggle.domain.chat.entity;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    @Builder
    public ChatRoom(String name, String description, String password, Member owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.password = password;
    }

    public void updateChatRoom(String name, String description, String password) {
        this.name = name;
        this.description = description;
        this.password = password;
    }

}
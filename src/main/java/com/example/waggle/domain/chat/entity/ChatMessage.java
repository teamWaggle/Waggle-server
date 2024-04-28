package com.example.waggle.domain.chat.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    private Long id;
    private Long chatRoomId;
    private String messageType;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String senderUserUrl;
    private LocalDateTime sendDate;
    private Integer readCount;

    public enum MessageType {
        ENTER, TALK
    }

}

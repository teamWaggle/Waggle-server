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
public class Chat {

    @Id
    private String id;
    private String chatRoomId;
    private Long senderId;
    private String senderUsername;
    private String contentType;
    private String content;
    private LocalDateTime sendDate;
    private Integer readCount;

}

package com.example.waggle.domain.chat.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Document(collection = "chatMessage")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String id;

    @NotNull
    private Long chatRoomId;

    @NotNull
    private MessageType messageType;

    @NotBlank
    private String content;

    @NotBlank
    private String senderUserUrl;

    @NotNull
    private Long sendTime;

    public enum MessageType {
        ENTER, TALK
    }

}

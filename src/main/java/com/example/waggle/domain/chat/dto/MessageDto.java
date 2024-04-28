package com.example.waggle.domain.chat.dto;

import com.example.waggle.domain.chat.entity.ChatMessage;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {

    private String id;
    @NotNull
    private Long chatRoomId;
    @NotNull
    private MessageType messageType;
    @NotNull
    private String content;
    @NotNull
    private String senderUserUrl;
    private String senderNickname;
    private String senderProfileImgUrl;
    private Long sendTime;

    public void setContent(String content) {
        this.content = content;
    }

    public void setSendTimeAndSenderInfo(LocalDateTime sendTime,
                                         String senderNickname,
                                         String senderProfileImgUrl) {
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.senderNickname = senderNickname;
        this.senderProfileImgUrl = senderProfileImgUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .messageType(ChatMessage.MessageType.valueOf(messageType.toString()))
                .content(content)
                .senderUserUrl(senderUserUrl)
                .sendTime(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .build();
    }

    public enum MessageType {
        ENTER, TALK
    }
}
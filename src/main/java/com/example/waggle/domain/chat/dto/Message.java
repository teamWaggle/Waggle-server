package com.example.waggle.domain.chat.dto;

import com.example.waggle.domain.chat.entity.Chat;
import jakarta.validation.constraints.NotNull;
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
public class Message implements Serializable {
    private String id;
    @NotNull
    private String roomId;
    @NotNull
    private String contentType;
    @NotNull
    private String content;
    @NotNull
    private Long senderId;
    @NotNull
    private String senderUsername;
    private Long sendTime;
    private Integer readCount;

    public void setSendTimeAndSender(String senderUsername, Long senderId, LocalDateTime sendTime, Integer readCount) {
        this.senderUsername = senderUsername;
        this.senderId = senderId;
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.readCount = readCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chat toEntity() {
        return Chat.builder()
                .senderUsername(senderUsername)
                .senderId(senderId)
                .chatRoomId(roomId)
                .contentType(contentType)
                .content(content)
                .sendDate(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .readCount(readCount)
                .build();
    }

}

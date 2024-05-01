package com.example.waggle.web.dto.chat;

import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.chat.entity.ChatMessageType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    private String id;
    @NotNull
    private Long chatRoomId;
    @NotNull
    private ChatMessageType chatMessageType;
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
                .chatMessageType(ChatMessageType.valueOf(chatMessageType.toString()))
                .content(content)
                .senderUserUrl(senderUserUrl)
                .sendTime(sendTime)
                .build();
    }
}